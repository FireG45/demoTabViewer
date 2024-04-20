import * as React from 'react';
import Popover from '@mui/material/Popover';
import Typography from '@mui/material/Typography';
import InfoIcon from '@mui/icons-material/Info';
import {Button, Divider, IconButton, Stack} from '@mui/material';
import {useCookies} from 'react-cookie';
import {useNavigate} from "react-router-dom";
import ReportDialog from "./ReportDialog";
import DeleteDialogButtonAdmin from "./DeleteDialogButtonAdmin";
import {useState} from "react";
import getRole from "./utils/getRole";

export default function TabInfoPopover({data = {user: null, uploaded: null, owner: false}}) {
    const [anchorEl, setAnchorEl] = React.useState(null);
    // const [owner, setOwner] = React.useState(false);
    const [cookies, setCookie, removeCookie] = useCookies(["token"]);
    const [role, setRole] = useState("");
    const navigate = useNavigate();

    const handleClick = async (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    fetch('http://localhost:8080/auth/role', {
        headers: new Headers({
            'Authorization': 'Bearer ' + cookies["token"]
        })
    }).then((response) => {
        response.text().then((res) => setRole(res));
    });

    const open = Boolean(anchorEl);
    const id = open ? 'simple-popover' : undefined;

    console.log("DATA: ", data);

    const handleDelte = async () => {
        try {
            const result = await fetch("http://localhost:8080/tabs/delete/" + data.id, {
                method: "DELETE",
                headers: new Headers({
                    'Authorization': 'Bearer ' + cookies["token"]
                })
            });

            if (result.ok) {
                handleClose();
            } else {
                handleClose();
            }
            navigate('/');
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <div>
            <IconButton aria-describedby={id} aria-label="info" size="large"
                        onClick={handleClick}><InfoIcon/></IconButton>
            <Popover
                id={id}
                open={open}
                anchorEl={anchorEl}
                onClose={handleClose}
                anchorOrigin={{
                    vertical: 'bottom',
                    horizontal: 'left',
                }}
            >
                <Typography sx={{p: 2}}>
                    <Stack>
                        <p>Загрузил: {data.user}</p>
                        <p>Дата загрузки: {data.uploaded}</p>
                        { role === "ADMIN" || data.owner ?
                            <>
                                <Button onClick={() => navigate('/edit/' + data.id + '/' + data.track)}
                                        variant="outlined"
                                        color="info">
                                    Редактировать
                                </Button>
                                <br/>
                            </>
                            :
                            <></>
                        }
                        {cookies["user"] ?
                            <>
                                <Divider/>
                                <br/>
                                { role === "ADMIN" ?
                                    <DeleteDialogButtonAdmin id={data.id} deleteHandler={handleDelte}/>
                                    :
                                    <ReportDialog id={data.id}/>
                                }
                            </>
                            :
                            <></>
                        }
                    </Stack>

                </Typography>
            </Popover>
        </div>
    );
}
