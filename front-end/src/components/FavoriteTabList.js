import { Box, Button, ButtonGroup, Divider, Grid, IconButton, List, ListItemButton, ListItemText, Rating, Stack, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import { useCookies } from "react-cookie";
import { useNavigate } from "react-router-dom";
import EditIcon from '@mui/icons-material/Edit';
import DeleteDialogButton from "./DeleteDialogButton";
import NoTabsCard from "./NoTabsCard";
import styled from "@emotion/styled";
import CircleOutlinedIcon from '@mui/icons-material/CircleOutlined';
import CircleIcon from '@mui/icons-material/Circle';
import Checkbox from '@mui/material/Checkbox';
import FavoriteBorder from '@mui/icons-material/FavoriteBorder';
import Favorite from '@mui/icons-material/Favorite';
import HeartBrokenIcon from '@mui/icons-material/HeartBroken';
import { log } from "vexflow";

function FavoriteTabList() {
  const [tabs, setTabs] = useState([]);
  const [changed, setChanged] = useState(false);
  const navigate = useNavigate();
  const [cookies, setCookie, removeCookie] = useCookies(["token"]);

  const deleteHandler = (id) => {
    setTabs(tabs.filter(t => t.id != id))
  }

  useEffect(() => {
    document.title = 'Избранные табулатуры';
    fetch('http://localhost:8080/tabs/favorite', {
      headers: new Headers({
        'Authorization': 'Bearer ' + cookies["token"]
      })
    })
      .then((response) => response.json())
      .then((data) => {
        setTabs(data);
        setChanged(false);
      })
      .catch((err) => {
        console.log(err.message);
      });
  }, [changed]);

  const paperSX = {
    boxShadow: 3,
    "&:hover": {
      bgcolor: "#ebebeb",
      boxShadow: 8,
    },
  };


  const StyledRating = styled(Rating)({
    '& .MuiRating-iconFilled': {
      color: '#2979ff',
    },
    '& .MuiRating-iconHover': {
      color: '#2979ff',
    },
  });

  return (
    <Stack spacing={5}>
      {tabs.length ?
        <>
          <Typography variant="h4" align={'center'} alignSelf={'center'}>
            <br />
            Избранные табулатуры:
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
                              primary={index + 1 + ". " + tab.title + " - " + tab.author}
                              secondary={
                                <StyledRating name="read-only" size="small"
                                  icon={<CircleIcon fontSize="inherit" color="green" />}
                                  emptyIcon={<CircleOutlinedIcon fontSize="inherit" color="green" />} value={tab.rating} readOnly />
                              }
                            />
                          </ListItemButton>
                        </Grid>
                        <Grid alignSelf={'center'} xs={1}>
                          <ButtonGroup
                            disableElevation
                            variant="contained"
                            aria-label="Disabled elevation buttons"
                          >
                            <IconButton onClick={async () => {
                              console.log(tab.id);
                              try {
                                const result = await fetch(`http://localhost:8080/tabs/removefavorite/` + tab.id, {
                                  method: "DELETE",
                                  headers: new Headers({
                                    'Content-Type': 'application/json',
                                    'Authorization': 'Bearer ' + cookies["token"]
                                  })
                                });
                                if (result.ok) {
                                  setChanged(true);
                                }
                              } catch (error) {
                                console.error(error);
                              }
                            }} checked={tab.favorite}>
                              <HeartBrokenIcon />
                            </IconButton>
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
            У вас нет избранных табулатур :{'('}
          </Typography>
          <Button variant={'contained'} onClick={() => navigate('/Back Home')}>
            На главную
          </Button>
        </Stack>
      }
    </Stack>
  );
}
export default FavoriteTabList;