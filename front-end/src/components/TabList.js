import { Paper } from "@mui/material";
import { useEffect, useState } from "react";

function TabList() {

    const [tabs, setTabs] = useState([]);
    useEffect(() => {
        fetch('http://localhost:8080')
            .then((response) => response.json())
            .then((data) => {
                console.log(data);
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
                <Paper elevation='8' style={{margin:"10px",padding:"15px", textAlign:"left"}}>
                    <h3 className="post-title">
                        <a href={'http://localhost:8080/tabs/' + tab.id}>{tab.author} - {tab.title}</a>
                    </h3>
                </Paper>
            </div>
         );
      })}
   </div>
    );  
}
  export default TabList;