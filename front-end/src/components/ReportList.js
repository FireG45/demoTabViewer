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
import {Link, useNavigate} from "react-router-dom";
import styled from "@emotion/styled";
import CircleOutlinedIcon from '@mui/icons-material/CircleOutlined';
import CircleIcon from '@mui/icons-material/Circle';
import HeartBrokenIcon from '@mui/icons-material/HeartBroken';
import Loading from "./Loading";
import DeleteIcon from "@mui/icons-material/Delete";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import TextField from "@mui/material/TextField";
import DialogActions from "@mui/material/DialogActions";
import * as React from "react";

export default function ReportList() {
    const [reports, setReports] = useState([]);
    const [loaded, setLoaded] = useState(false);
    const [changed, setChanged] = useState(false);
    const navigate = useNavigate();
    const [cookies, setCookie, removeCookie] = useCookies(["token"]);
    const [open, setOpen] = React.useState(false);

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };


    useEffect(() => {
        document.title = 'Жалобы';
        fetch('http://localhost:8080/reports', {
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
            setReports(data);
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
            {reports.length ?
                <>
                    <Typography variant="h4" align={'center'} alignSelf={'center'}>
                        <br/>
                        Жалобы на табулатуры:
                    </Typography>
                    <Box fullwidth>
                        <List>
                            <Stack spacing={2}>
                                {reports.map((report, index) => {
                                    return (
                                        <>
                                            <Grid container>
                                                <Grid xs={11}>
                                                    <ListItemButton id={report.id}
                                                                    onClick={() => handleClickOpen()}>
                                                        <ListItemText aria-multiline={true}
                                                                      primary={
                                                                          report.tab.title + " - " + report.tab.author
                                                                      }
                                                                      secondary={String(report.message.slice(0, 100)) +
                                                                          (report.message.length > 100 ? "..." : "")}
                                                        />
                                                    </ListItemButton>
                                                </Grid>

                                                <Dialog
                                                    open={open}
                                                    onClose={handleClose}
                                                    PaperProps={{
                                                        component: 'form',
                                                        onSubmit: (event) => {
                                                            event.preventDefault();
                                                            navigate('/tabs/' + report.tab.id + '/0');
                                                            handleClose();
                                                        },
                                                    }}
                                                >
                                                    <DialogTitle>
                                                        Жалоба от пользователя {report.user.username}
                                                    </DialogTitle>
                                                    <DialogContent>
                                                        <TextField
                                                            autoFocus
                                                            aria-readonly={true}
                                                            margin="dense"
                                                            id="name"
                                                            name="message"
                                                            label="Описание проблемы"
                                                            type="textarea"  // Change type to "textarea" for multi-line input
                                                            fullWidth
                                                            sx={{width: 500}} // Adjust width as needed (in pixels)
                                                            variant="standard"
                                                            multiline
                                                            rows={6}
                                                            value={report.message}
                                                        />
                                                    </DialogContent>
                                                    <DialogActions>
                                                        <Button onClick={handleClose}>Отмена</Button>
                                                        <Button type="submit" color={'success'}>Открыть</Button>
                                                    </DialogActions>
                                                </Dialog>

                                                <Grid alignSelf={'center'} xs={1}>
                                                    <ButtonGroup
                                                        disableElevation
                                                        variant="contained"
                                                        aria-label="Disabled elevation buttons"
                                                    >
                                                        <IconButton onClick={async () => {
                                                            console.log(report.id);
                                                            try {
                                                                const result = await fetch(`http://localhost:8080/reports/` + report.id, {
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
                                                        }} checked={report.favorite}>
                                                            <DeleteIcon/>
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
                                    Нет жалоб :{')'}
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
