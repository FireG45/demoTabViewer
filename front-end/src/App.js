import PrimarySearchAppBar from './components/PrimarySearchAppBar';
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import TabList from './components/TabList';
 
function App() {
    return (
        <Router>
            <PrimarySearchAppBar/>
            <Routes>
                <Route path='/' exact element={<TabList />} />
            </Routes>
        </Router>
    );
}
 
export default App;