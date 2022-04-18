import styled from "@emotion/styled";

const Info = () => {

  return (
    <Container>
      <h1>Ruby's Page</h1>
    </Container>
    )
}
export default Info;


const Container = styled.div`
  width: 400px;

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