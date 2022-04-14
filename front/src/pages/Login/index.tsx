import { Box} from "./styles";

const oAuthList = [
  {
    id: `google`,
    text: "Google Login",
  },
  // {
  //   id: "naver",
  //   text: "Naver Login",
  // },
];

const Login = () => {

  return (
    <Box>
      <h1>Ruby's Page</h1>
      {oAuthList.map((oAuth) => (
        <a
          key={oAuth.id}
          href={`${process.env.REACT_APP_SERVER_URL}/oauth2/authorization/${oAuth.id}?redirect_uri=${process.env.REACT_APP_FRONT_URL}`}
        >
          {oAuth.text}
        </a>
      ))}
    </Box>
    )
}
export default Login;

/** 자주 쓰이는 컴포넌트라면 공통으로 관리. 그렇지 않다면 별도 폴더에서 관리 */