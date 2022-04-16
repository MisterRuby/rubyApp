import styled from "@emotion/styled";
import { Link } from "react-router-dom";

const Left = () : JSX.Element => {
  return (
    <Container>
      <Link to="/boards">
        Board
      </Link>
    </Container>
  )
}
export default Left;

const Container = styled.div`
  position: absolute;
  top: 0;
  width: 300px;
  height: 100%;
  padding-top: 100px;
  display: flex;
  flex-direction: column;
  align-items: center;

  border-right: 1px solid black;
  box-sizing: border-box;

  & > a {
    width: 180px;
    font-size: 50px;
    text-decoration: none;
    color: black;
    cursor: pointer;
  }
`