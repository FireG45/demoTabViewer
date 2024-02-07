import * as React from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { useNavigate } from 'react-router-dom';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import { Divider } from '@mui/material';
import DeleteAccountDialogButton from '../DeleteAccountDialogButton';
import { useCookies } from 'react-cookie';
import { Alert, Stack } from '@mui/material';

const defaultTheme = createTheme();

export default function Account() {
    const [cookies, setCookie, removeCookie] = useCookies();
    const navigate = useNavigate()
    const [loaded, setLoaded] = React.useState(false);
    const [email, setEmail] = React.useState("");
    const [username, setUsername] = React.useState("");
    const [initialUser, setInitialUser] = React.useState("");
    const [error, setError] = React.useState(null);

    const handleSubmit = async (event) => {
        event.preventDefault();
        const formData = new FormData(event.currentTarget);

        try {
            const result = await fetch("http://localhost:8080/auth/update", {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + cookies["token"],
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(
                    {
                        username: formData.get('username'),
                        email: formData.get('email'),
                    })
            });

            const data = await result.json();

            console.log(data.username)

            if (result.ok) {
                if (data.errorMessages) {
                    console.log(data.errorMessages);
                    setError(
                        <>
                            <Stack spacing={3}>
                                {data.errorMessages.map((element, ind) => {
                                    return (
                                        <Alert key={ind} severity="error">
                                            {element}
                                        </Alert>
                                    )
                                })}
                            </Stack>
                            <br />
                        </>
                    )
                } else {
                    setCookie("user", data.username);
                    window.location.reload();
                    //navigate('/signin')
                }
            } else {
                setError(<><Alert severity="error">Неверный email или пароль!</Alert><br /></>);
              }
        } catch (error) {
            console.error(error);
        }
    };

    const getParams = async () => {
        try {
            const result = await fetch("http://localhost:8080/auth/update", {
                method: "GET",
                headers: new Headers({
                    'Authorization': 'Bearer ' + cookies["token"],
                })
            });

            if (result.ok) {
                const data = await result.json();
                setEmail(data.email)
                setUsername(data.username)
                setInitialUser(data.username)
                console.log({
                    email: email,
                    username: username,
                });
                setLoaded(true)
            }
        } catch (error) {
            console.error(error);
        }
    }

    React.useEffect(() => {
        if (!loaded) getParams()
    })

    return (
        <ThemeProvider theme={defaultTheme}>
            <Container component="main" maxWidth="xs">
                <CssBaseline />
                <Box
                    sx={{
                        marginTop: 8,
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                    }}
                >
                    <Avatar sx={{ m: 1, bgcolor: '#2979ff' }}>
                        <AccountCircleIcon />
                    </Avatar>
                    <Typography component="h1" variant="h5">
                        Аккаунт
                    </Typography>
                    <Box component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 3 }}>
                        <Grid container spacing={2}>
                            <Grid item xs={12}>
                                <TextField
                                    autoComplete="name"
                                    name="username"
                                    required
                                    fullWidth
                                    id="username"
                                    label="Имя пользователя"
                                    autoFocus
                                    value={username}
                                    onChange={(e) => {
                                        setUsername(e.target.value)
                                    }}
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    required
                                    fullWidth
                                    id="email"
                                    label="Email"
                                    name="email"
                                    autoComplete="email"
                                    value={email}
                                    onChange={(e) => {
                                        setEmail(e.target.value)
                                    }}
                                />
                            </Grid>
                        </Grid>
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2 }}
                        >
                            Сохранить
                        </Button>
                        <br />
                        <Divider />
                        <br />
                        <DeleteAccountDialogButton username={initialUser} />
                        <br />
                        <Button
                            fullWidth
                            color={'info'}
                            variant={'outlined'}
                            sx={{ mt: 3, mb: 2 }}
                            onClick={async () => {
                                removeCookie("token")
                                removeCookie("user")
                                await fetch("http://localhost:8080/auth/logout", {
                                    method: 'POST',
                                    headers: new Headers({
                                        'Authorization': 'Bearer ' + cookies["token"]
                                    })
                                })
                                navigate('/signin');
                            }}
                        >
                            Выйти
                        </Button>
                        {error}
                    </Box>
                </Box>
            </Container>
        </ThemeProvider>
    );
}
