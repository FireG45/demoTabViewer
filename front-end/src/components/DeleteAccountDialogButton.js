import * as React from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import { Alert, IconButton, Snackbar } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import { Link, useNavigate } from 'react-router-dom';
import { useCookies } from 'react-cookie';

export default function DeleteAccountDialogButton({ username = null }) {
    const [open, setOpen] = React.useState(false);
    const navigate = useNavigate();
    const [cookies, setCookie, removeCookie] = useCookies(["token"]);
    const [openSnack, setOpenSnack] = React.useState(false);

    const handleClickSnack = () => {
      setOpenSnack(true);
    };
  
    const handleCloseSnack = (event, reason) => {
      if (reason === 'clickaway') {
        return;
      }
  
      setOpenSnack(false);
    };

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleDelte = async () => {
        try {
            const result = await fetch("http://localhost:8080/auth/delete/" + username, {
                method: "DELETE",
                headers: new Headers({
                    'Authorization': 'Bearer ' + cookies["token"]
                })
            });

            if (result.ok) {
                handleClose();
                removeCookie("user")
                removeCookie("token")
            } else {
                handleClickSnack();
                handleClose();
            }
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <React.Fragment>
            <Snackbar open={openSnack} autoHideDuration={6000} onClose={handleCloseSnack}>
                <Alert
                    onClose={handleCloseSnack}
                    severity="error"
                    variant="filled"
                    sx={{ width: '100%' }}
                >
                    Ошибка! Нет доступа!
                </Alert>
            </Snackbar>
            <Link onClick={handleClickOpen}>
                Удалить аккаунт
            </Link>
            <Dialog
                open={open}
                onClose={handleClose}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
            >
                <DialogTitle id="alert-dialog-title">
                    {"Удалить аккаунт?"}
                </DialogTitle>
                <DialogContent>
                    <DialogContentText id="alert-dialog-description">
                        Вы уверены что хотите удалить свой аккаунт? Это действие нельзя отменить!
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleDelte} color='error'>
                        Да
                    </Button>
                    <Button onClick={handleClose} autoFocus color='info'>
                        Нет
                    </Button>
                </DialogActions>
            </Dialog>
        </React.Fragment>
    );
}
