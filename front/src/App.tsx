import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Login from './pages/Login';

const App = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path='/' element={<Login/>}/>            {/** 로그인/main */}
        
        {/* <Route path='/' element={<Left/>}/> */}
        <Route path='/board'>               {/** 목록화면 */}
          <Route path=':boardId'></Route>   {/** 상세화면 */}
          <Route path='add'></Route>        {/** 등록화면 */}
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;

/** 
 * router url 은 서버단 url과는 별도. 애초에 host 자체가 다름 
 * <Link > 컴포넌트는 to 프로퍼티에 지정한 주소로 routing 해주는 a태그의 역할. 내부에 태그들을 담아서 보여주는 컴포넌트가 아님!
 * 
 * 
 * */
