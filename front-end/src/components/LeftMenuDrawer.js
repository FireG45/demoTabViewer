import * as React from 'react';
import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import List from '@mui/material/List';
import Divider from '@mui/material/Divider';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import MenuIcon from '@mui/icons-material/Menu';
import { IconButton } from '@mui/material';
import LogoutIcon from '@mui/icons-material/Logout';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import Favorite from '@mui/icons-material/Favorite';
import PlaylistAddIcon from '@mui/icons-material/PlaylistAdd';
import { useCookies } from 'react-cookie';
import { useNavigate } from 'react-router-dom';
import LoginIcon from '@mui/icons-material/Login';
import UploadFileIcon from '@mui/icons-material/UploadFile';

export default function LeftMenuDrawer() {
  const [state, setState] = React.useState({
    top: false,
    left: false,
    bottom: false,
    right: false,
  });

  const toggleDrawer = (anchor, open) => (event) => {
    if (event.type === 'keydown' && (event.key === 'Tab' || event.key === 'Shift')) {
      return;
    }

    setState({ ...state, [anchor]: open });
  };

  const [cookies, setCookie, removeCookie] = useCookies(["token"]);
  const navigate = useNavigate()
  const [user, setUser] = React.useState(cookies["user"]);

  React.useEffect(() => {
    setUser(cookies["user"])
  }, [cookies["user"]])

  const list = (anchor) => (
    <Box
      sx={{ width: anchor === 'top' || anchor === 'bottom' ? 'auto' : 250 }}
      role="presentation"
      onClick={toggleDrawer(anchor, false)}
      onKeyDown={toggleDrawer(anchor, false)}
    >
      <List>
        {cookies["token"] ?
          <>
            <ListItem disablePadding>
            <ListItemButton onClick={() => navigate("/account")}>
                <ListItemIcon>
                  <AccountCircleIcon />
                </ListItemIcon>
                <ListItemText primary={user} />
              </ListItemButton>
            </ListItem>

            <ListItem disablePadding>
              <ListItemButton onClick={() => navigate("/favorite")}>
                <ListItemIcon>
                  <Favorite />
                </ListItemIcon>
                <ListItemText primary={"Избранные табулатуры"} />
              </ListItemButton>
            </ListItem>

            <ListItem disablePadding>
              <ListItemButton onClick={() => navigate("/mytabs")}>
                <ListItemIcon>
                  <PlaylistAddIcon />
                </ListItemIcon>
                <ListItemText primary={"Мои табулатуры"} />
              </ListItemButton>
            </ListItem>

            <ListItem disablePadding>
              <ListItemButton  onClick={() => navigate("/upload")}>
                <ListItemIcon>
                  <UploadFileIcon />
                </ListItemIcon>
                <ListItemText primary={"Загрузить табулатуру"} />
              </ListItemButton>
            </ListItem>
          </>
          :
          <>
            <ListItem disablePadding>
              <ListItemButton onClick={() => navigate("/signin")}>
                <ListItemIcon>
                  <LoginIcon />
                </ListItemIcon>
                <ListItemText primary={"Войти"} />
              </ListItemButton>
            </ListItem>
          </>
        }
      </List>
      {cookies["token"]  ?
        <>
          <Divider />
          <List>
            <ListItem disablePadding>
              <ListItemButton onClick={ async () => {
                removeCookie("token")
                removeCookie("user")
                await fetch("http://localhost:8080/auth/logout", {
                  method : 'POST',
                  headers : new Headers({
                    'Authorization': 'Bearer ' + cookies["token"]
                  })
                })
                navigate('/signin');
              }}>
                <ListItemIcon>
                  <LogoutIcon />
                </ListItemIcon>
                <ListItemText primary={"Выйти"} />
              </ListItemButton>


            </ListItem>
          </List>
        </> :
        <></>
      }


    </Box>
  );

  return (
    <div>
      <React.Fragment key={'left'}>
        <IconButton
          size="large"
          edge="start"
          color="inherit"
          aria-label="open drawer"
          onClick={toggleDrawer('left', true)}
          sx={{ mr: 2 }}
        >
          <MenuIcon />
        </IconButton>
        <Drawer
          anchor={'left'}
          open={state['left']}
          onClose={toggleDrawer('left', false)}
        >
          {list('left')}
        </Drawer>
      </React.Fragment>
    </div>
  );
}