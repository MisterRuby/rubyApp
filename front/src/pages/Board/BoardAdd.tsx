import styled from "@emotion/styled";
import axios from "axios";
import { Dispatch, SetStateAction, useCallback, useRef, useState } from "react";
import useSWR from "swr";
import AccountType from "../../types/account/AccountType";
import fetcher from "../../utils/fetcher";

const MAX_FILE_SIZE : number = parseInt(`${process.env.REACT_APP_MAX_FILE_SIZE}`);
const extSet = new Set(["txt", "png", "img", "pdf", "xls"]);

const BoardAdd = () => {
  const titleRef = useRef<HTMLInputElement>(null);
  const contentRef = useRef<HTMLTextAreaElement>(null);
  const commentContentRef = useRef<HTMLInputElement>(null);
  const [fileList, setFileList] : [fileList:File[], setFileList:Dispatch<SetStateAction<File[]>>] = useState<File[]>([]);
  const [fileCount, setFileCount] = useState(0);
  const [fileSize, setFileSize] = useState(0);

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

    const formData = new FormData();

    formData.delete("files");
    fileList.forEach(file => {
      if (file) {
        formData.append("files", file);
      }
    });
    formData.delete("board");
    formData.append("board", new Blob([JSON.stringify({
      title: title,
      content: content
    })], { type: "application/json" }));

    const res = await axios.post(`${process.env.REACT_APP_SERVER_URL}/boards`, formData,
    {
      withCredentials: true,
      headers : {
        "Content-Type" : "multipart/form-data",
      }
    }
    );

    if (res.data && res.data.id) {
      alert("글이 등록되었습니다.");
      document.location.replace(`/boards/${res.data.id}`);
    }
  }, [fileList]);

  

  const onChangeFileList = useCallback(() => {
    const files = commentContentRef.current?.files;
    const beforeList = fileList;
  
    if (files?.length && files?.length + fileCount > 5) {
      alert("파일은 최대 5개까지만 업로드 할 수 있습니다.");
      return;
    }
    
    if (files?.length) {
      let beforeFileSize = fileSize;
      for (let i = 0; i < files?.length; i++) {
        beforeFileSize += files[i].size;
        if(beforeFileSize > MAX_FILE_SIZE) {
          alert("파일 업로드 용량은 최대 20MB를 초과할 수 없습니다.");
          return;
        }

        const ext = files[i].name.substring(files[i].name.lastIndexOf('.') + 1);
        if (!extSet.has(ext)) {
          alert("txt, png, img, pdf, xls 이외의 확장자인 파일은 저장할 수 없습니다.");
          return;
        }
      }

      for (let i = 0; i < files?.length; i++) {
        beforeList.push(files[i]);
      }
      setFileSize(beforeFileSize);
      setFileList([...beforeList]);
      setFileCount(fileCount + files?.length);
    }
  }, [fileCount, fileList, fileSize]);

  const onDeleteFile = useCallback((idx:number) =>{
    const beforeList = fileList;
    setFileSize(fileSize - beforeList[idx].size);
    delete beforeList[idx];
    setFileList([...beforeList]);
    setFileCount(fileCount - 1);
  }, [fileCount, fileList, fileSize])

  return (
    account &&
    <Container>
      <h2>게시글 등록</h2>
      <input placeholder="제목을 입력해주세요." ref={titleRef}/>
      <textarea placeholder="내용을 입력해주세요." ref={contentRef}/>
      <div>
        파일 등록 : <input type="file" multiple placeholder="파일 등록" ref={commentContentRef} onChange={onChangeFileList}/>
      </div>
      {
        fileList.map((file:File, idx:number) => file && (
          <div key={file.name + idx}>
            <span>{file.name}</span>
            <button onClick={() => onDeleteFile(idx)}>삭제</button>
          </div>
        ))
      }
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

  & > div > input {
    border: 1px solid black;
    border-radius: 5px;
    background-color: white;
  }
`;