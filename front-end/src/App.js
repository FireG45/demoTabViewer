import PrimarySearchAppBar from './components/PrimarySearchAppBar';
import React, {useState} from 'react';
import {BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import TabList from './components/TabList';
import ShowTab from './components/ShowTab';
import UploadTab from './components/UploadTab';
import NotFoundError from './components/NotFoundError';
import SignUp from './components/auth/SignUp';
import SignIn from './components/auth/SignIn';
import {useCookies} from 'react-cookie';
import MyTabList from './components/MyTabList';
import UpdateTab from './components/UpdateTab';
import Account from './components/auth/Account';
import FavoriteTabList from './components/FavoriteTabList';
import EditTab from "./components/EditTab";
import {SnackbarProvider} from "notistack";
import getRole from "./components/utils/getRole";
import ReportList from "./components/ReportList";
import TermsDialog from "./components/TermsDialog";

const SERVER_NAME = "localhost"

function App() {
    const [cookies, setCookie, removeCookie] = useCookies(["token"]);
    const [role, setRole] = useState("");
    console.log(cookies["token"]);

    //setRole(getRole(cookies["token"]));

    console.log(process.env.SERVER_NAME)

    return (
        <React.StrictMode>
            <Router>
                <SnackbarProvider>
                    <PrimarySearchAppBar/>
                    <br/><br/><br/><br/>
                    <TermsDialog/>
                    <Routes>
                        <Route path='/' exact element={<TabList/>}/>
                        <Route path='/tabs/:id/:track' element={<ShowTab/>}/>
                        <Route path='/edit/:id/:track' element={<EditTab/>}/>
                        <Route path='/upload' element={!cookies["token"] ? <SignIn/> : <UploadTab/>}/>
                        <Route path='/signup' element={<SignUp/>}/>
                        <Route path='/signin' element={<SignIn/>}/>
                        <Route path='/mytabs' element={!cookies["token"] ? <SignIn/> : <MyTabList/>}/>
                        <Route path='/favorite' element={!cookies["token"] ? <SignIn/> : <FavoriteTabList/>}/>
                        <Route path='/update/:id' element={!cookies["token"] ? <SignIn/> : <UpdateTab/>}/>
                        <Route path='/tabs/:author' element={<TabList/>}/>
                        <Route path='/account' element={!cookies["token"] ? <SignIn/> : <Account/>}/>
                        <Route path='*' element={<NotFoundError/>}/>
                        <Route path='/reports'
                               element={<ReportList/>}
                        />
                    </Routes>
                </SnackbarProvider>
            </Router>
        </React.StrictMode>
    );
}

export default App;