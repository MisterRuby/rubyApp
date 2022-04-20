import styled from "@emotion/styled";
import axios from "axios";
import { useCallback, useRef, useState } from "react";
import { useParams } from "react-router-dom";
import useSWR from "swr";
import AccountType from "../../types/account/AccountType";
import { BoardType, CommentType } from "../../types/board/BoardType";
import fetcher from "../../utils/fetcher";

const BoardInfo = () : JSX.Element => {
  const params = useParams();
  const [boardId] = useState(params.boardId);
  const commedntContentRef = useRef<HTMLTextAreaElement>(null);

  const {data, mutate} = useSWR<BoardType>(`${process.env.REACT_APP_SERVER_URL}/boards/${boardId}`, fetcher , {
    dedupingInterval : 0,
    revalidateOnFocus : false
  });

  const account : AccountType = useSWR(`${process.env.REACT_APP_SERVER_URL}/accounts`, fetcher, {
    dedupingInterval : 1000 * 60 * 5,
  }).data;

  const onDeleteBoard = useCallback(async(boardId:number) => {
    const res = await axios.delete(`${process.env.REACT_APP_SERVER_URL}/boards/${boardId}`, 
      {withCredentials: true}
    );
    if (res.data && res.data.id) {
      alert("게시글이 삭제되었습니다.");
      document.location.replace(`/boards/list/0`);
    }
  }, []);

  const onAddComment = useCallback(async() => {
    const res = await axios.post(`${process.env.REACT_APP_SERVER_URL}/comments`, {
      content : commedntContentRef.current?.value,
      boardId : boardId
    },
    {withCredentials: true}
    );
    if (res.data && res.data.id) {
      alert("댓글이 등록되었습니다.");
      const board = fetcher(`${process.env.REACT_APP_SERVER_URL}/boards/${boardId}`);
      mutate(board, false);
    }
  }, [boardId, mutate]);

  const onDeleteComment = useCallback(async(commentId:number) => {
    const res = await axios.delete(`${process.env.REACT_APP_SERVER_URL}/comments/${commentId}`, 
      {withCredentials: true}
    );
    if (res.data && res.data.id) {
      alert("댓글이 삭제되었습니다.");
      const board = fetcher(`${process.env.REACT_APP_SERVER_URL}/boards/${boardId}`);
      mutate(board, false);
    }
  }, [boardId, mutate]);

  // TODO - 게시글 수정, 댓글 수정
  // 댓글 수정 버튼 클릭시 textarea 가 활성화되고 기존 입력된 내용의 영역은 비활성화

  const onMoveBoardUpdate = useCallback(() => {
    document.location.replace(`/boards/${boardId}/update`);
  }, [boardId]);

  return (
    !data ? <></> :
    <Container>
      <h2>제목 : {data.title}</h2>
      <span>글쓴이 : {data.name}</span>
      <span>등록일 : {data.reportingDate}</span>
      {data.email === account.email && <button onClick={onMoveBoardUpdate}>수정</button>}
      {data.email === account.email && <button onClick={() => onDeleteBoard(data.id)}>삭제</button>}
      <hr />
      <p>{data.content}</p>
      <hr />

      {
        data.commentList && data.commentList.length > 0 &&
        <CommentList>
          <h3>댓글</h3>
          {
            data.commentList &&
            data.commentList.map((comment:CommentType) => (
              <div key={`comment${comment.id}`}>
                <span>{comment.name}</span>
                <span>{comment.reportingDate}</span>
                {account.email === comment.email && <button>수정</button>}
                {account.email === comment.email && <button onClick={() => onDeleteComment(comment.id)}>삭제</button>}
                <p>{comment.content}</p>
              </div>
            ))
          }
          <hr />
        </CommentList>
      }
      {
        account &&
        <CommentWrite>
          <span>{account.name}</span>
          <textarea placeholder="댓글 쓰기" ref={commedntContentRef}></textarea>
          <button onClick={onAddComment}>등록</button>
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

  & > div > button {
    cursor: pointer;
    border: none;
    background-color: white;
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