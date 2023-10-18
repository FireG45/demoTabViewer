import { useParams } from "react-router-dom";
import { useState, useEffect } from "react";
import Score from "./Score";
import Stack from "@mui/material/Stack";
import { Container } from "@mui/material";

function ShowTab() {
    var id = useParams().id;
    const [tab, setTab] = useState(0)
    var path = "http://localhost:8080/tabs/" + id

    useEffect(() => {
      fetch(path)
      .then(response => {
        return response.json()
      })
      .then(data => {
        setTab(data)
      })
    }, [path])

    const centerStyle = ({ display: 'flex', alignItems: 'center', justifyContent: 'center' })
    return (
      <Container style={centerStyle}>
        <Stack spacing={1}>
          <h2 style={centerStyle}>{tab.author} - {tab.title}</h2>
          <Score style={centerStyle} id={id}/>
        </Stack>
      </Container>
    );
}

export default ShowTab;