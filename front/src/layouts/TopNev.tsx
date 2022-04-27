import styled from "@emotion/styled";
import axios from "axios";
import { useCallback } from "react";
import { Link } from "react-router-dom";
import useSWR from "swr";
import fetcher from "../utils/fetcher";

const oAuthList = [
  {
    id: `google`,
    text: "Google Login",
  },
  {
    id: "naver",
    text: "Naver Login",
  },
];

const TopNev = () : JSX.Element => {
  const {data, mutate} = useSWR(`${process.env.REACT_APP_SERVER_URL}/accounts/check`, fetcher, {
    dedupingInterval : 1000 * 60 * 5,
  });

  const onLogout = useCallback(() => {
    axios.delete(`${process.env.REACT_APP_SERVER_URL}/accounts/logout`, {
      withCredentials: true,
    })
    .then(()=> {
      // 로그아웃 요청시 data 를 바로 바꿔줘야함
      mutate(undefined, false);
    })
  }, [mutate]);

  return(
    <Container>
      <Home to="/">
        Home
      </Home>
      {data ?
        <Logout onClick={onLogout}>로그아웃</Logout> :
        oAuthList.map((oAuth) => (
          <a
            key={oAuth.id}
            href={`${process.env.REACT_APP_SERVER_URL}/oauth2/authorization/${oAuth.id}?redirect_uri=${process.env.REACT_APP_FRONT_URL}`}
          >
            {oAuth.text}
          </a>
        ))
      }
    </Container>
  )
}

export default TopNev;

const Container = styled.div`
  position: fixed;
  width: 100%;
  height: 45px;
  text-align : right;
  box-sizing: border-box;
  padding: 10px 20px;
  border-bottom: 1px solid black;
  background: white;
  z-index: 9998;

  & > a {
    font-size: 18px;
    text-decoration: none;
    margin-left: 15px;
  }
`;

const Home = styled(Link)`
  margin-right: 20px;
`

const Logout = styled.span`
  cursor: pointer;
`