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
import { Alert, Stack } from '@mui/material';
import { useCookies } from "react-cookie";

const defaultTheme = createTheme();


export default function SignIn() {
  const navigate = useNavigate();
  const [error, setError] = React.useState(null);
  const [cookies, setCookie, removeCookie] = useCookies(["token"]);

  const handleSubmit = async (event) => {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);

    console.log({
      email: formData.get('email'),
      password: formData.get('password'),
    });

    try {
      const result = await fetch("http://localhost:8080/auth/login", {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(
          {
            email: formData.get('email'),
            password: formData.get('password')
          })
      });

      if (result.ok) {
        const data = await result.json();

        if (data.errorMessages) {
          console.log(data.errorMessages);
          setError(
            <>
              <Stack spacing={3}>
                {data.errorMessages.map((element) => {
                  return (
                    <Alert severity="error">
                      {element}
                    </Alert>
                  )
                })}
              </Stack>
              <br />
            </>
          )
        } else {
          setCookie("token", data.accessToken)
          setCookie("user", data.username)
          navigate('/')
        }
      } else {
        setError(<><Alert severity="error">Неверный email или пароль!</Alert><br /></>);
      }
    } catch (error) {
      console.error(error);
    }
  };
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
          <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Войти
          </Typography>
          <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="email"
              label="Email"
              name="email"
              autoComplete="email"
              autoFocus
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Пароль"
              type="password"
              id="password"
              autoComplete="current-password"
            />
            {/* <FormControlLabel
              control={<Checkbox value="remember" color="primary" />}
              label="Remember me"
            /> */}
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Войти
            </Button>
            {error}
            <Grid container spacing={5}>
              <Grid item>
                <Link href="/signup" variant="body2">
                  {"Еще не зарегестрированы? Зарегестрироваться"}
                </Link>
              </Grid>
            </Grid>
          </Box>
        </Box>
      </Container>
    </ThemeProvider>
  );
}
