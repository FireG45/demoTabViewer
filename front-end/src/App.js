import PrimarySearchAppBar from './components/PrimarySearchAppBar';
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import TabList from './components/TabList';
import ShowTab from './components/ShowTab';
import UploadTab from './components/UploadTab';
 
function App() {
    return (
        <React.StrictMode>
            <Router>
                <PrimarySearchAppBar/>
                <Routes>
                    <Route path='/' exact element={<TabList/>} />
                    <Route path='/tabs/:id/:track' element={<ShowTab/>} />
                    <Route path='/upload' element={<UploadTab/>} />
                </Routes>
            </Router>
        </React.StrictMode>
    );
}
 
export default App;