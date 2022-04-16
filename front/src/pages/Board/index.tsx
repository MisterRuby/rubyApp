import styled from "@emotion/styled";
import List from "./list";

const Board = () => {

  return (
    <Container>
      <List />
    </Container>
    )
}
export default Board;

const Container = styled.div`
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`