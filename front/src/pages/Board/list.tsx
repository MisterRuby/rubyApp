import { css } from "@emotion/react";
import styled from "@emotion/styled";
import axios from "axios";
import { useCallback, useRef, useState } from "react";
import { Link } from "react-router-dom";
import useSWR from "swr";

const SEARCH_TYPES = [
  { value: "TITLE", text: "제목" },
  { value: "CONTENT", text: "내용" },
  { value: "USERNAME", text: "이름" },
];


const List = () : JSX.Element => {
  const [searchType, setSearchType] = useState<string>(SEARCH_TYPES[0].value);
  const [searchWord, setSearchWord] = useState<string>('');
  const [boardList, setBoardList] = useState([]);
  const [pageNum, setPageNum] = useState<number>(0);
  const [totalPages, setTotalPages] = useState<number>(0);
  const [pageNumList, setPageNumList] = useState<number[]>([]);

  const searchTypeRef = useRef<HTMLSelectElement>(null);
  const searchWordRef = useRef<HTMLInputElement>(null);

  const initPageNumList = useCallback((pageNum:number, totalPages:number) => {
    const startNum = Math.floor(pageNum / 10) * 10;
    const endNum = startNum + 9 < totalPages - 1 ? startNum + 9 : totalPages - 1;
    const numArr = [];
    for (let i = startNum; i <= endNum; i++) {
      numArr.push(i);
    }
    setPageNumList(numArr);
  }, []);

  const boardFetcher = (url : string, searchType:string, searchWord:string, pageNum:number) => 
    axios.get(url, {
      params : {searchType : searchType, searchWord: searchWord, pageNum: pageNum},
      withCredentials: true,
    })
    .then(res => {
      setBoardList(res.data.boardList);
      setPageNum(res.data.pageNum);
      setTotalPages(res.data.totalPages);
      initPageNumList(res.data.pageNum, res.data.totalPages);

      return res.data;
    });

  const {data} = useSWR(`${process.env.REACT_APP_SERVER_URL}/boards`, (url) => boardFetcher(url, searchType, searchWord, pageNum), {
    dedupingInterval : 1000 * 60 * 5,
  });

  const handlePageNum = (pageNum:number) => {
    boardFetcher(`${process.env.REACT_APP_SERVER_URL}/boards`, searchType, searchWord, pageNum );
  }

  return (
    <ListBox>
      <ListHead>
        <h2>Board</h2>
        <div>
          <select id="searchType" ref={searchTypeRef}>
            {SEARCH_TYPES.map((type) => (
              <option key={type.value} value={type.value}>
                {type.text}
              </option>
            ))}
          </select>
          <input id="searchWord" ref={searchWordRef}/>
          <button onClick={() => {
            let type = searchTypeRef.current?.value;
            type = type ? type : SEARCH_TYPES[0].value;
            setSearchType(type);

            let word = searchWordRef.current?.value;
            word = word ? word : '';
            setSearchWord(word);

            boardFetcher(`${process.env.REACT_APP_SERVER_URL}/boards`, type, word, 0);
          }}>검색</button>
        </div>
      </ListHead>
      <BoardList>
        {data &&
          boardList.map((board:any) => (
            <Link
              to={`/boards/${board.id}`}
              key={board.id}
            >
              <div>
                <span>{board.id}</span>
                <span>{board.title}</span>
              </div>
              <div>
                <span>{board.reportingDate}</span>
                <span>작성자 : {board.name}</span>
              </div>
            </Link>
          ))
        }
      </BoardList>
      <PagingBar>
        {data && pageNum >= 10 && 
          <PageButton
            key={"page" + (Math.floor(pageNum / 10) * 10 - 1)}
            value={Math.floor(pageNum / 10) * 10 - 1}
            onClick={() => {
              handlePageNum(Math.floor(pageNum / 10) * 10 - 1);
            }}   
          >
            ◀
          </PageButton>
        }
        {
          data && pageNumList.map(index => (
            <PageButton 
              key={"page" + index}
              value={index}
              color={index === pageNum ? 'crimson' : 'black'}
              onClick={() => {
                handlePageNum(index);
              }}            
            >
              {index + 1}
            </PageButton>
          ))
        }
        {data && ((Math.floor(pageNum / 10) + 1 ) * 10 <= totalPages) && 
          <PageButton
            key={"page" + ((Math.floor(pageNum / 10) + 1)* 10)}
            value={(Math.floor(pageNum / 10) + 1)* 10}
            onClick={() => {
              handlePageNum((Math.floor(pageNum / 10) + 1)* 10);
            }}  
          >
            ▶
          </PageButton>
        }
      </PagingBar>
    </ListBox>
  )
};

export default List;

const ListBox = styled.div`
  width: 1000px;
  height: 800px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  justify-content: center;
`;

const ListHead = styled.div`
  position: relative;

  & > h2 {
    display: inline-block;
    position: relative;
    left: 50%;
    transform: translateX(-50%);
    box-sizing: content-box;
  }

  & > div {
    text-align: center;

    & > * {
      margin: 2px;
    }

    & > select {
      width: 70px;
    }

    & > button {
      width: 60px;
      border: 1px solid black;
      border-radius: 5px;
      background-color: white;
    }
  }
`;

const BoardList = styled.div`
  & > a {
    position: relative;
    display: block;
    color: black;
    border-bottom: 1px solid black;
    box-sizing: border-box;
    padding: 5px 0;
  }

  & > a > div > span {
    display: inline-block;
    box-sizing: border-box;
    margin-left: 10px;
  }
`

const PagingBar = styled.div`
  position: relative;
  width: fit-content;
  left: 50%;
  transform: translateX(-50%);
  margin-top: 10px;
`;

const PageButton = styled.button`
  width: 24px;
  height: 24px;
  line-height: 24px;
  font-size: 18px;
  margin: 2px;
  border: none;
  background-color: white;

  ${
    props => props.color &&
    css`
      color: ${props.color};
    `
  }
`