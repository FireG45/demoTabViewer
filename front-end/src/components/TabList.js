import { Paper } from "@mui/material";
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

function TabList() {
    const [tabs, setTabs] = useState([]);
    useEffect(() => {
        fetch('http://localhost:8080')
            .then((response) => response.json())
            .then((data) => {
                setTabs(data);
            })
            .catch((err) => {
                console.log(err.message);
            });
    }, []);

    return (
        <div className="tabs-container">
      {tabs.map((tab) => {
         return (
            <div className="post-card" key={tab.id}>
                <Paper elevation={5} style={{margin:"10px",padding:"15px", textAlign:"left"}}>
                    <h3 className="post-title">
                        <Link to={'http://localhost:3000/tabs/' + tab.id +'/0'}>{tab.author} - {tab.title}</Link>
                    </h3>
                </Paper>
            </div>
         );
      })}
   </div>
    );  
}
  export default TabList;