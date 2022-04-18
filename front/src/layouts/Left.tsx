import styled from "@emotion/styled";
import { Link } from "react-router-dom";

const Left = () : JSX.Element => {

  return (
    <Container>
      <Link to="/">
        Main
      </Link>
      <Link to="/boards">
        Board
      </Link>
    </Container>
  )
}
export default Left;

const Container = styled.div`
  position: fixed;
  top: 45px;
  width: 300px;
  height: calc(100% - 45px);
  padding-top: 100px;
  display: flex;
  flex-direction: column;
  align-items: center;
  border-right: 1px solid black;
  box-sizing: border-box;
  background: white;
  z-index: 9999;

  & > a {
    width: 180px;
    font-size: 50px;
    text-decoration: none;
    color: black;
    cursor: pointer;
  }
`