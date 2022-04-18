import { BrowserRouter} from 'react-router-dom';
import Left from './layouts/Left';
import Main from './layouts/Main';
import TopNev from './layouts/TopNev';

const App = () => {

  return (
    <>
      <BrowserRouter>
        <TopNev />
        <Left />
        <Main />
      </BrowserRouter>
    </>
  );
}

export default App;

/** 
 * router url 은 서버단 url과는 별도. 애초에 host 자체가 다름 
 * <Link > 컴포넌트는 to 프로퍼티에 지정한 주소로 routing 해주는 a태그의 역할. 내부에 태그들을 담아서 보여주는 컴포넌트가 아님!
 * 
 * 
 * */
