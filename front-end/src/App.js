import PrimarySearchAppBar from './components/PrimarySearchAppBar';
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import TabList from './components/TabList';
import ShowTab from './components/ShowTab';
import UploadTab from './components/UploadTab';
import NotFoundError from './components/NotFoundError';
import SignUp from './components/SignUp';
import SignIn from './components/SignIn';
 
function App() {
    return (
        <React.StrictMode>
            <Router>
                <PrimarySearchAppBar/>
                <Routes>
                    <Route path='/' exact element={<TabList/>} />
                    <Route path='/tabs/:id/:track' element={<ShowTab/>} />
                    <Route path='/upload' element={<UploadTab/>} />
                    <Route path='/signup' element={<SignUp/>} />
                    <Route path='/signin' element={<SignIn/>} />
                    <Route path='*' element={<NotFoundError/>} />
                </Routes>
            </Router>
        </React.StrictMode>
    );
}
 
export default App;