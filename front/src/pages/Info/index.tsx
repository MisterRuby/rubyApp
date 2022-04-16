import styled from "@emotion/styled";

const Info = () => {

  return (
    <Container>
      <h1>Ruby's Page</h1>
    </Container>
    )
}
export default Info;


export const Container = styled.div`
  width: 400px;
  height: 100px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translateX(-50%) translateY(-50%);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  & > h1 {
    font-size: 60px;
  }

  & > a {
    width: 100%;
    height: 50px;
    text-align: center;
    line-height: 50px;
    text-decoration: none;
    font-size: 40px;
    font-weight: 500;
    color: black;
    margin: 10px;
  }
`;