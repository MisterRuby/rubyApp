import styled from "@emotion/styled";
import { Route, Routes } from "react-router-dom";
import BoardInfo from "../pages/Board/BoardInfo";
import BoardList from "../pages/Board/Boardlist";
import BoardPost from "../pages/Board/BoardPost";
import Info from "../pages/Info";

const Main = () => {

  return (
    <Container>
      <Routes>
        <Route path='/' element={<Info/>}/>
        <Route path='boards' element={<BoardList/>}/>
        <Route path='boards/:boardId' element={<BoardInfo/>}/> 
        <Route path='boards/add' element={<BoardPost/>}/> 
      </Routes>
    </Container>
    )
}
export default Main;

const Container = styled.div`
  position: relative;
  top: 45px;
  width: 100%;
  height: calc(100% - 45px);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  box-sizing: border-box;
`