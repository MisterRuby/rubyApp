import { BrowserRouter, Route, Routes } from 'react-router-dom';
import useSWR from 'swr';
import Left from './layouts/Left';
import TopNev from './layouts/TopNev';
import Board from './pages/Board';
import Info from './pages/Info';
import fetcher from './utils/fetcher';

const App = () => {

  const {data} = useSWR(`${process.env.REACT_APP_SERVER_URL}/accounts`, fetcher, {
    dedupingInterval : 1000 * 60 * 5,    // 해당 시간을 간격으로 요청함. 간격 사이의 요청은 캐시에서 가져옴
  });

  return (
    <div>
      <BrowserRouter>
        <TopNev />
        <Left />
        <Routes>
          <Route path='/' element={<Info/>}/>            {/** 로그인/main */}
          
          {/* <Route path='/' element={<Left/>}/> */}
          <Route path='/boards' element={<Board/>}>               {/** 목록화면 */}
            <Route path=':boardId'></Route>   {/** 상세화면 */}
            <Route path='add'></Route>        {/** 등록화면 */}
          </Route>
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;

/** 
 * router url 은 서버단 url과는 별도. 애초에 host 자체가 다름 
 * <Link > 컴포넌트는 to 프로퍼티에 지정한 주소로 routing 해주는 a태그의 역할. 내부에 태그들을 담아서 보여주는 컴포넌트가 아님!
 * 
 * 
 * */
