import { Box, Button, ButtonGroup, Divider, Grid, IconButton, List, ListItemButton, ListItemText, Stack, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import { useCookies } from "react-cookie";
import { useNavigate } from "react-router-dom";
import EditIcon from '@mui/icons-material/Edit';
import DeleteDialogButton from "./DeleteDialogButton";
import NoTabsCard from "./NoTabsCard";

function MyTabList() {
  const [tabs, setTabs] = useState([]);
  const navigate = useNavigate();
  const [cookies, setCookie, removeCookie] = useCookies(["token"]);

  const deleteHandler = (id) => {
    setTabs(tabs.filter(t => t.id != id))
  }

  useEffect(() => {
    document.title = 'Tabulatures';
    fetch('http://localhost:8080/tabs/mytabs', {
      headers: new Headers({
        'Authorization': 'Bearer ' + cookies["token"]
      })
    })
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
      bgcolor: "#ebebeb",
      boxShadow: 8,
    },
  };

  return (
    <Stack spacing={5}>
      {tabs.length ?
        <>
          <Typography variant="h4" align={'center'} alignSelf={'center'}>
            <br />
            Загруженные табулатуры:
          </Typography>
          <Box fullwidth>
            <List>
              <Stack spacing={2}>
                {tabs.map((tab, index) => {
                  return (
                    <>
                      <Grid container >
                        <Grid xs={11}>
                          <ListItemButton id={tab.id} onClick={() => navigate('/tabs/' + tab.id + '/0')}>
                            <ListItemText
                              primary={index + 1 + ". " + tab.title}
                              secondary={tab.author}
                            />
                          </ListItemButton>
                        </Grid>
                        <Grid alignSelf={'center'} xs={1}>
                          <ButtonGroup
                            disableElevation
                            variant="contained"
                            aria-label="Disabled elevation buttons"
                          >
                            <IconButton onClick={() => navigate('/update/' + tab.id)} variant={'outlined'} color={'warning'}>
                              <EditIcon />
                            </IconButton>
                            <DeleteDialogButton id={tab.id} deleteHandler={() => deleteHandler(tab.id)} />
                          </ButtonGroup>
                        </Grid>
                      </Grid>
                      <Divider variant="inset" component="li" />
                    </>
                  );
                })}
              </Stack>
            </List>

          </Box>
        </>
        :
        <Stack spacing={5} alignContent={'center'} alignItems={'center'}>
          <Typography variant="h4" align={'center'} alignSelf={'center'}>
            <br />
            У вас нет загруженных табулатур :{'('}
          </Typography>
          <Button variant={'contained'} onClick={() => navigate('/upload')}>
            Загрузить
          </Button>
        </Stack>
      }
    </Stack>
  );
}
export default MyTabList;