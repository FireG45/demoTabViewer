import {
    Box,
    Button,
    ButtonGroup,
    Divider,
    Grid,
    IconButton,
    List,
    ListItemButton,
    ListItemText,
    Rating,
    Stack,
    Typography
} from "@mui/material";
import {useEffect, useState} from "react";
import {useCookies} from "react-cookie";
import {useNavigate} from "react-router-dom";
import styled from "@emotion/styled";
import CircleOutlinedIcon from '@mui/icons-material/CircleOutlined';
import CircleIcon from '@mui/icons-material/Circle';
import HeartBrokenIcon from '@mui/icons-material/HeartBroken';
import Loading from "./Loading";

function FavoriteTabList() {
    const [tabs, setTabs] = useState([]);
    const [loaded, setLoaded] = useState(false);
    const [changed, setChanged] = useState(false);
    const navigate = useNavigate();
    const [cookies, setCookie, removeCookie] = useCookies(["token"]);

    useEffect(() => {
        document.title = 'Избранные табулатуры';
        fetch('http://localhost:8080/tabs/favorite', {
            headers: new Headers({
                'Authorization': 'Bearer ' + cookies["token"]
            })
        }).then((response) => {
                if (response.status === 403 || response.status === 401) {
                    setCookie("token", null)
                    setCookie("user", null)
                    navigate('/signin');
                    return;
                }
                return response.json();
            }).then((data) => {
                setTabs(data);
                setLoaded(true);
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
                        <br/>
                        Избранные табулатуры:
                    </Typography>
                    <Box fullwidth>
                        <List>
                            <Stack spacing={2}>
                                {tabs.map((tab, index) => {
                                    return (
                                        <>
                                            <Grid container>
                                                <Grid xs={11}>
                                                    <ListItemButton id={tab.id}
                                                                    onClick={() => navigate('/tabs/' + tab.id + '/0')}>
                                                        <ListItemText
                                                            primary={index + 1 + ". " + tab.title + " - " + tab.author}
                                                            secondary={
                                                                <StyledRating name="read-only" size="small"
                                                                              icon={<CircleIcon fontSize="inherit"
                                                                                                color="green"/>}
                                                                              emptyIcon={<CircleOutlinedIcon
                                                                                  fontSize="inherit" color="green"/>}
                                                                              value={tab.rating} readOnly/>
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
                                                            <HeartBrokenIcon/>
                                                        </IconButton>
                                                    </ButtonGroup>
                                                </Grid>
                                            </Grid>
                                            <Divider variant="inset" component="li"/>
                                        </>
                                    );
                                })}
                            </Stack>
                        </List>

                    </Box>
                </>
                :
                <Stack spacing={5} alignContent={'center'} alignItems={'center'}>
                    {
                        loaded
                            ?
                            <>
                                <Typography variant="h4" align={'center'} alignSelf={'center'}>
                                    <br/>
                                    У вас нет избранных табулатур :{'('}
                                </Typography>
                                <Button variant={'contained'} onClick={() => navigate('/')}>
                                    На главную
                                </Button>
                            </>
                            :
                            <Loading/>
                    }
                </Stack>
            }
        </Stack>
    );
}

export default FavoriteTabList;