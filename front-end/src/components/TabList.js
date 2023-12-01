    import { Paper } from "@mui/material";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function TabList() {
    const [tabs, setTabs] = useState([]);
    const navigate = useNavigate();
    useEffect(() => {
        document.title = 'Tabulatures';
        fetch('http://localhost:8080')
            .then((response) => response.json())
            .then((data) => {
                setTabs(data);
            })
            .catch((err) => {
                console.log(err.message);
            });
    }, []);

    const paperSX = {
        boxShadow: 3,
        "&:hover": {
          bgcolor: "#ccc",
          boxShadow: 8,
        },
      };

    return (
        <div className="tabs-container">
      {tabs.map((tab) => {
         return (
            <div className="post-card" key={tab.id}>
                <Paper onClick={() => navigate('/tabs/' + tab.id +'/0')} sx={paperSX} elevation={5} style={{margin:"10px",padding:"15px", textAlign:"left"}}>
                   <h3> {tab.author} - {tab.title} </h3>
                </Paper>
            </div>
         );
      })}
   </div>
    );  
}
  export default TabList;