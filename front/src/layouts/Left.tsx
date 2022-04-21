import { css } from "@emotion/react";
import styled from "@emotion/styled";
import { useCallback, useState } from "react";
import { Link } from "react-router-dom";

const Left = () : JSX.Element => {
  const [openMenu, setOpenMenu] = useState(true);
  const onOpenMenu = useCallback(() => {
    setOpenMenu(!openMenu);
  }, [openMenu])

  return (
    <Container openMenu={openMenu} onClick={onOpenMenu}>
      <Link to="/boards">
        Board
      </Link>
    </Container>
  )
}
export default Left;

const containerStyle = (props:{openMenu:boolean}) => css`
  position: fixed;
  top: 45px;
  left: ${props.openMenu ? 0 : -250}px;
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
  transition: 0.3s;

  & > a {
    width: 180px;
    font-size: 50px;
    text-decoration: none;
    color: black;
    cursor: pointer;
  }
`

const Container = styled.div`
  ${containerStyle}
`