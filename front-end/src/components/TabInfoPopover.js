import * as React from 'react';
import Popover from '@mui/material/Popover';
import Typography from '@mui/material/Typography';
import InfoIcon from '@mui/icons-material/Info';
import {Button, Divider, IconButton, Stack} from '@mui/material';
import {useCookies} from 'react-cookie';
import {useNavigate} from "react-router-dom";

export default function TabInfoPopover({data = {user: null, uploaded: null, owner: false}}) {
    const [anchorEl, setAnchorEl] = React.useState(null);
    // const [owner, setOwner] = React.useState(false);
    const [cookies, setCookie, removeCookie] = useCookies(["token"]);
    const navigate = useNavigate();

    const handleClick = async (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    const open = Boolean(anchorEl);
    const id = open ? 'simple-popover' : undefined;

    console.log("DATA: ", data);

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
                        {data.owner ?
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
                        <Divider/>
                        <Button variant="outlined" color="error">
                            Пожаловаться
                        </Button>
                    </Stack>

                </Typography>
            </Popover>
        </div>
    );
}
