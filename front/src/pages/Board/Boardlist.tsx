import { css } from "@emotion/react";
import styled from "@emotion/styled";
import axios from "axios";
import { useCallback, useRef, useState } from "react";
import { Link} from "react-router-dom";
import useSWR from "swr";
import AccountType from "../../types/account/AccountType";
import { BoardListType, BoardType } from "../../types/board/BoardType";
import fetcher from "../../utils/fetcher";

const SEARCH_TYPES = [
  { value: "TITLE", text: "제목" },
  { value: "CONTENT", text: "내용" },
  { value: "USERNAME", text: "이름" },
];

declare global {
  interface Window {
    boardSearchType: string;
    boardSearchWord: string;
    boardPageNum: number;
  }
}

const SearchTypeSWR = () => {
  const {data, mutate} = useSWR('boardSearchType', () => window.boardSearchType, {
    dedupingInterval : 0,
    revalidateOnFocus : false
  });
  return {
    searchType: data,
    searchTypeMutate: (type:string) => {
      window.boardSearchType = type;
      return mutate();
    }
  }
}

const SearchWordSWR = () => {
  const {data, mutate} = useSWR('boardSearchWord', () => window.boardSearchWord, {
    dedupingInterval : 0,
    revalidateOnFocus : false
  });
  return {
    searchWord: data,
    searchWordMutate: (word:string) => {
      window.boardSearchWord = word;
      return mutate();
    }
  }
}

const PageNumSWR = () => {
  const {data, mutate} = useSWR('boardPageNum', () => window.boardPageNum, {
    dedupingInterval : 0,
    revalidateOnFocus : false
  });
  return {
    pageNum: data,
    pageNumMutate: (pageNum:number) => {
      window.boardPageNum = pageNum;
      return mutate();
    }
  }
}

const BoardList = () : JSX.Element => {

  const searchTypeRef = useRef<HTMLSelectElement>(null);
  const searchWordRef = useRef<HTMLInputElement>(null);

  const {searchType, searchTypeMutate} = SearchTypeSWR();
  const {searchWord, searchWordMutate} = SearchWordSWR();
  const {pageNum, pageNumMutate} = PageNumSWR();
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

  const searchFetcher = useCallback(
    async ({searchType, searchWord, pageNum} : {searchType:string | undefined, searchWord:string | undefined, pageNum:number | undefined}) => {

    const res = await axios.get(`${process.env.REACT_APP_SERVER_URL}/boards`, {
      params : {
        searchType: searchType || SEARCH_TYPES[0].value,
        searchWord: searchWord || '',
        pageNum: pageNum || 0
      },
      withCredentials: true,
    });
    initPageNumList(res.data.pageNum, res.data.totalPages);

    return res.data;
  }, [initPageNumList]);
    
  const {data, mutate} = useSWR<BoardListType>(`${process.env.REACT_APP_SERVER_URL}/boards`, () => searchFetcher({searchType, searchWord, pageNum}), {
    dedupingInterval : 0,
    revalidateOnFocus : false
  });

  const account : AccountType = useSWR(`${process.env.REACT_APP_SERVER_URL}/accounts`, fetcher, {
    dedupingInterval : 1000 * 60 * 5,
  }).data;

  const onSearch = useCallback(() => {
    const type = searchTypeRef.current?.value || SEARCH_TYPES[0].value;
    const word = searchWordRef.current?.value || '';
    searchTypeMutate(type);
    searchWordMutate(word);
    pageNumMutate(0);
    mutate(searchFetcher({searchType, searchWord, pageNum}), false);
  }, [mutate, pageNum, pageNumMutate, searchFetcher, searchType, searchTypeMutate, searchWord, searchWordMutate]);

  const onPageNum = useCallback((pageNum:number) => {
    pageNumMutate(pageNum);
    mutate(searchFetcher({ searchType, searchWord, pageNum}), false);
  }, [mutate, pageNumMutate, searchFetcher, searchType, searchWord]);

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
        {account && <Link to='/boards/add'>새 글 등록</Link>}
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