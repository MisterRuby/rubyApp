import styled from "@emotion/styled";
import { Link } from "react-router-dom";

const Left = () : JSX.Element => {
  return (
    <LeftBox>
      <Link to="/board">
        Board
      </Link>
    </LeftBox>
  )
}
export default Left;

const LeftBox = styled.div`
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