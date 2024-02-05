import PrimarySearchAppBar from './components/PrimarySearchAppBar';
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import TabList from './components/TabList';
import ShowTab from './components/ShowTab';
import UploadTab from './components/UploadTab';
import NotFoundError from './components/NotFoundError';
import SignUp from './components/auth/SignUp';
import SignIn from './components/auth/SignIn';
import { useCookies } from 'react-cookie';
import MyTabList from './components/MyTabList';
import UpdateTab from './components/UpdateTab';
 
function App() {
    const [cookies, setCookie, removeCookie] = useCookies(["token"]);

    return (
        <React.StrictMode>
            <Router>
                <PrimarySearchAppBar/>
                <Routes>
                    <Route path='/' exact element={<TabList/>} />
                    <Route path='/tabs/:id/:track' element={<ShowTab/>} />
                    <Route path='/upload' element={cookies["token"] ? <UploadTab/> : <SignIn/>} />
                    <Route path='/signup' element={<SignUp/>} />
                    <Route path='/signin' element={<SignIn/>} />
                    <Route path='/mytabs' element={<MyTabList/>} />
                    <Route path='/update/:id' element={<UpdateTab/>} />
                    <Route path='*' element={<NotFoundError/>} />
                </Routes>
            </Router>
        </React.StrictMode>
    );
}
 
export default App;