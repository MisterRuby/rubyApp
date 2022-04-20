import styled from "@emotion/styled";
import axios from "axios";
import React, { useCallback, useRef, useState } from "react";
import { useParams } from "react-router-dom";
import useSWR from "swr";
import AccountType from "../../types/account/AccountType";
import fetcher from "../../utils/fetcher";

const BoardUpdate = () => {
  const params = useParams();
  const [boardId] = useState(params.boardId);

  const titleRef = useRef<HTMLInputElement>(null);
  const contentRef = useRef<HTMLTextAreaElement>(null);

  const {data, mutate} = useSWR(`${process.env.REACT_APP_SERVER_URL}/boards/${boardId}`, fetcher , {
    dedupingInterval : 0,
    revalidateOnFocus : false
  });

  const account : AccountType = useSWR(`${process.env.REACT_APP_SERVER_URL}/accounts`, fetcher, {
    dedupingInterval : 1000 * 60 * 5,
  }).data;

  const onChangeTitle = useCallback(() => {
    mutate({...data, title : titleRef.current?.value}, false);
  }, [data, mutate])

  const onChangeContent = useCallback(() => {
    mutate({...data, content : contentRef.current?.value}, false);
  }, [data, mutate])

  const onSubmit = useCallback(async () => {
    const title = titleRef.current?.value;
    const content = contentRef.current?.value;

    if (!title || title.length < 2) {
      alert("제목은 두 글자 이상이어야 합니다.");
      return;
    } 
    if (!content || content.length < 2) {
      alert("내용은 두 글자 이상이어야 합니다.");
      return;
    }

    const res = await axios.patch(`${process.env.REACT_APP_SERVER_URL}/boards/${boardId}`, {
      title: titleRef.current?.value,
      content: contentRef.current?.value
    },
    {withCredentials: true}
    );

    if (res.data && res.data.id) {
      alert("글이 수정되었습니다.");
      document.location.replace(`/boards/${res.data.id}`);
    }
  }, [boardId]);

  return (
    data && account &&
    <Container>
      <h2>게시글 수정</h2>
      <input placeholder="제목을 입력해주세요." ref={titleRef} onChange={onChangeTitle} value={data.title}/>
      <textarea placeholder="내용을 입력해주세요." ref={contentRef} onChange={onChangeContent} value={data.content}/>
      <button onClick={onSubmit}>수정</button>
    </Container>
  );
}

export default BoardUpdate;

const Container = styled.div`
  padding: 10px;
  width: 800px;
  height: 80%;
  box-sizing: border-box;

  & > input {
    width: 780px;
    height: 30px;
    line-height: 30px;
    margin-bottom: 10px;
    box-sizing: border-box;
  }

  & > textarea {
    width: 780px;
    height: 500px;
    box-sizing: border-box;
    resize: none;
  }

  & > button {
    position: relative;
    left: 100%;
    transform: translateX(-100%);
    border: 1px solid black;
    border-radius: 5px;
    background-color: white;
}
`;