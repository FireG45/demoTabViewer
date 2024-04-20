import * as React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {useCookies} from "react-cookie";

export default function ReportDialog({id = 0}) {
    const [open, setOpen] = React.useState(false);
    const [cookies, setCookie, removeCookie] = useCookies(["token"]);

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    return (
        <React.Fragment>
            <Button variant="outlined" color="error" onClick={handleClickOpen}>
                Пожаловаться
            </Button>
            <Dialog
                open={open}
                onClose={handleClose}
                PaperProps={{
                    component: 'form',
                    onSubmit: (event) => {
                        event.preventDefault();
                        const formData = new FormData(event.currentTarget);
                        const formJson = Object.fromEntries(formData.entries());
                        const message = formJson.message;

                        try {
                            const result = fetch(`http://localhost:8080/reports/` + id, {
                                method: "POST",
                                headers: new Headers({
                                    'Content-Type': 'application/json',
                                    'Authorization': 'Bearer ' + cookies["token"]
                                }),
                                body: message,
                            });
                        } catch (error) {
                            console.error(error);
                        }
                        handleClose();
                    },
                }}
            >
                <DialogTitle>Пожаловаться</DialogTitle>
                <DialogContent>
                    <TextField
                        autoFocus
                        required
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
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Отмена</Button>
                    <Button type="submit" color={"error"}>Отправить</Button>
                </DialogActions>
            </Dialog>
        </React.Fragment>
    );
}
