import styled from "@emotion/styled";
import axios from "axios";
import { useParams } from "react-router-dom";
import useSWR from "swr";
import fetcher from "../../utils/fetcher";

interface BoardType {
  content: string,
  email: string,
  id: number,
  name: string,
  reportingDate: string,
  title: string,
  commentList: []
}

interface CommentType {
  boardId: number,
  content: string,
  email: string,
  id: number,
  name: string,
  reportingDate: string,
}

interface UserType {
  email: string,
  name: string,
}


const BoardInfo = () : JSX.Element => {
  const params = useParams();
  const boardId = params.boardId;

  const boardInfoFetcher = async (url : string) => {
    const res = await axios.get(url, {
      withCredentials: true,
    });
    return res.data;
  }



  const board : BoardType = useSWR(`${process.env.REACT_APP_SERVER_URL}/boards/${boardId}`, boardInfoFetcher , {
    dedupingInterval : 0,
    revalidateOnFocus : false
  }).data;

  const user : UserType = useSWR(`${process.env.REACT_APP_SERVER_URL}/accounts`, fetcher, {
    dedupingInterval : 1000 * 60 * 5,    // 해당 시간을 간격으로 요청함. 간격 사이의 요청은 캐시에서 가져옴
  }).data;

  console.log(user);

  // TODO - data 에 담긴 정보들을 통해서 화면에 보여주자!
  // 게시글 제목, 작성자 name, 작성일자, 게시글 본문, 댓글 목록

  // content: "게시글22의 본문입니다."
  // email: "test22@naver.com"
  // id: 134
  // name: "test22"
  // reportingDate: "2022-04-18"
  // title: "게시글22"

  return (
    board &&
    <Container>
      <h2>제목 : {board.title}</h2>
      <span>글쓴이 : {board.name}</span>
      <span>등록일 : {board.reportingDate}</span>
      <hr />
      <p>{board.content}</p>
      <hr />

      <CommentList>
        <h3>댓글</h3>
        {
          board.commentList.map((comment:CommentType) => (
            <div key={`comment${comment.id}`}>
              <span>{comment.name}</span>
              <span>{comment.reportingDate}</span>
              <p>{comment.content}</p>
            </div>
          ))
        }
      </CommentList>
      <hr />
      {
        user &&
        <CommentWrite>
          <span>{user.name}</span>
          <textarea placeholder="댓글 쓰기"></textarea>
          <button>등록</button>
        </CommentWrite>
      }
    </Container>
  )
}

export default BoardInfo;

const Container = styled.div`
  padding: 10px;
  width: 800px;
  height: 80%;
  box-sizing: border-box;

  & > h2 {
    margin: 10px 0;
  }

  & > span {
    display: inline-block;
    margin-right: 20px;
  }

  & > p {
    min-height: 200px;
    margin: 10px 0;
  }
`;

const CommentList = styled.div`
  margin: 20px 0;

  & > h3 {
    margin: 10px 0;
  }

  & > div > span {
    margin-right: 20px;
  }
`;

const CommentWrite = styled.div`
  padding-bottom: 100px;

  & > textarea {
    width: 780px;
    height: 100px;
    box-sizing: border-box;
    resize: none;
  }

  & > button {
    position: relative;
    left: 100%;
    transform: translateX(-100%);
  }
`;