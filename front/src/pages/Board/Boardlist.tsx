import { css } from "@emotion/react";
import styled from "@emotion/styled";
import axios from "axios";
import { useCallback, useRef, useState } from "react";
import { Link} from "react-router-dom";
import useSWR, { KeyedMutator, SWRResponse } from "swr";

const SEARCH_TYPES = [
  { value: "TITLE", text: "제목" },
  { value: "CONTENT", text: "내용" },
  { value: "USERNAME", text: "이름" },
];

interface BoardSearchType {
  searchType : string,
  searchWord : string,
  pageNum : number
}

interface BoardListType {
  boardList : [],
  pageNum: number,
  totalPages: number
}

interface BoardType {
  content: string,
  id: number,
  name: string,
  reportingDate: string,
  title: string
}

const BoardList = () : JSX.Element => {

  const searchTypeRef = useRef<HTMLSelectElement>(null);
  const searchWordRef = useRef<HTMLInputElement>(null);
  const [boardSearch, setBoardSearch] = useState<BoardSearchType>({
    searchType : SEARCH_TYPES[0].value,
    searchWord : '',
    pageNum : 0
  })
  const [pageNumList, setPageNumList] = useState<number[]>([]);

  const initPageNumList = useCallback((pageNum:number, totalPages:number) => {
    const startNum = Math.floor(pageNum / 10) * 10;
    const endNum = startNum + 9 < totalPages - 1 ? startNum + 9 : totalPages - 1;
    const numArr = [];
    for (let i = startNum; i <= endNum; i++) {
      numArr.push(i);
    }
    setPageNumList(numArr);
  }, []);

  const searchFetcher = useCallback(async () => {
    const res = await axios.get(`${process.env.REACT_APP_SERVER_URL}/boards`, {
      params : boardSearch,
      withCredentials: true,
    });
    initPageNumList(res.data.pageNum, res.data.totalPages);

    return res.data;
  }, [boardSearch, initPageNumList]);
    
  const {data, mutate} : SWRResponse<BoardListType, KeyedMutator<BoardListType>> = useSWR(`${process.env.REACT_APP_SERVER_URL}/boards`, () => searchFetcher(), {
    dedupingInterval : 0,
    revalidateOnFocus : false
  });
  const onSearch = useCallback(() => {
    const type = searchTypeRef.current?.value || SEARCH_TYPES[0].value;
    const word = searchWordRef.current?.value || '';
    setBoardSearch({...boardSearch, searchType : type, searchWord: word, pageNum : 0});
    mutate(searchFetcher(), true);
  }, [boardSearch, mutate, searchFetcher]);

  const onPageNum = useCallback((pageNum:number) => {
    setBoardSearch({...boardSearch, pageNum: pageNum});
    mutate(searchFetcher(), true);
  }, [boardSearch, mutate, searchFetcher]);

  return (
    <ListBox>
      <ListHeadBox>
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
          <button onClick={onSearch}>검색</button>
        </div>
        <Link to='/boards/add'>새 글 등록</Link>
      </ListHeadBox>
      {
        data && 
        <BoardListBox>
          {
            data.boardList.map((board:BoardType) => (
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
        </BoardListBox>
      }
      {
        data && 
        <PagingBar>
          {
            pageNumList.length > 0 && pageNumList[0] >= 10 && 
            <PageButton
              key={`page${pageNumList[0] - 1}`}
              value={pageNumList[0] - 1}
              onClick={() => {
                onPageNum(pageNumList[0] - 1);
              }}   
            >
              ◀
            </PageButton>
          }
          {
            pageNumList.map(index => (
              <PageButton 
                key={`page${index}`}
                value={index}
                color={index === data.pageNum ? 'crimson' : 'black'}
                onClick={() => {
                  onPageNum(index);
                }}            
              >
                {index + 1}
              </PageButton>
            ))
          }
          {
            pageNumList.length === 10 && pageNumList[9] + 1 < data.totalPages && 
            <PageButton
              key={`page${pageNumList[9] + 1}`}
              value={pageNumList[9] + 1}
              onClick={() => {
                onPageNum(pageNumList[9] + 1);
              }}  
            >
              ▶
            </PageButton>
          }
        </PagingBar>
      }
    </ListBox>
  )
};

export default BoardList;

const ListBox = styled.div`
  width: 1000px;
  height: 800px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  justify-content: center;
`;

const ListHeadBox = styled.div`
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

  & > a {
    position: absolute;
    right: 0;
    top: 100%;
    transform: translateY(-100%);
    cursor: pointer;
    text-decoration: none;
    color: black;
    border: 1px solid black;
    border-radius: 10px;
    padding: 5px;

  }
`;

const BoardListBox = styled.div`
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