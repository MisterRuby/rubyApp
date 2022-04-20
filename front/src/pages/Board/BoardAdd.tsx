import styled from "@emotion/styled";
import axios from "axios";
import { useCallback, useRef } from "react";
import useSWR from "swr";
import AccountType from "../../types/account/AccountType";
import fetcher from "../../utils/fetcher";

const BoardAdd = () => {
  const titleRef = useRef<HTMLInputElement>(null);
  const contentRef = useRef<HTMLTextAreaElement>(null);

  const account : AccountType = useSWR(`${process.env.REACT_APP_SERVER_URL}/accounts`, fetcher, {
    dedupingInterval : 1000 * 60 * 5,
  }).data;

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

    const res = await axios.post(`${process.env.REACT_APP_SERVER_URL}/boards`, {
      title: titleRef.current?.value,
      content: contentRef.current?.value
    },
    {withCredentials: true}
    );

    if (res.data && res.data.id) {
      alert("글이 등록되었습니다.");
      document.location.replace(`/boards/${res.data.id}`);
    }
  }, []);

  return (
    account &&
    <Container>
      <h2>게시글 등록</h2>
      <input placeholder="제목을 입력해주세요." ref={titleRef}/>
      <textarea placeholder="내용을 입력해주세요." ref={contentRef}/>
      <button onClick={onSubmit}>등록</button>
      
    </Container>
  );
}

export default BoardAdd;

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