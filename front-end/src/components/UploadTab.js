import * as React from 'react';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { Alert, Divider, Input, Stack } from '@mui/material';
import { Link, useNavigate } from 'react-router-dom';
import { useCookies } from "react-cookie";

export default function UploadTab() {
  const [cookies, setCookie, removeCookie] = useCookies(["token"]);
  const [file, setFile] = React.useState(null);
  const [title, setTitle] = React.useState(null);
  const [author, setAuthor] = React.useState(null);
  const [error, setError] = React.useState(null);

  const navigate = useNavigate();

  const handleFileChange = (e) => {
    if (e.target.files) {
      setFile(e.target.files[0]);
      if (e.target.files[0] && ['gp1', 'gp2', 'gp3', 'gp4', 'gp5'].indexOf(e.target.files[0].name.split('.').pop()) != -1) {
        setError(null)
      } else {
        setError(
          <><Alert severity="error">Неверный формат файла! Допустимы только форматы: .gp1, .gp2, .gp3, .gp4, .gp5!</Alert><br /></>
        )
      }
    }
  };

  const handleTitleChange = (e) => {
    if (e.target.value) {
      setTitle(e.target.value);
    }
  }

  const handleAuthorChange = (e) => {
    if (e.target.value) {
      setAuthor(e.target.value);
    }
  }

  const handleUpload = async (e) => {
    e.preventDefault()
    if (file && !error && author && title) {
      console.log("Uploading file...");
      const formData = new FormData();
      formData.append("file", file);
      formData.append("author", author);
      formData.append("title", title);

      try {
        const result = await fetch("http://localhost:8080/upload", {
          method: "POST",
          body: formData,
          headers: new Headers({
            'Authorization': 'Bearer ' + cookies["token"]
          })
        });


        if (result.ok) {
          const data = await result.json();
          navigate('/tabs/' + data.id + '/0')
        } else {
          if (result.status === 403) {
            setError(
              <Alert severity="error">
                Ошибка! Нет доступа!
                <br/>
                <Link to={'/signin'} onClick={() => {removeCookie("token"); removeCookie("user")}}>
                  Войти
                </Link>
              </Alert>);
          }
        }
      } catch (error) {
        console.error(error);
      }
    }
  };

  return (
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
        <Stack spacing={5}>
          <Typography component="h1" variant="h2">
            Загрузить табулатуру!
          </Typography>
          <Divider />
          <Typography component="h5">
            Есть табулатура, которой можно поделиться?
            Загрузите её в формате Guitar Pro и заполните поля ниже.
          </Typography>
          <Box component="form" onSubmit={handleUpload} sx={{ mt: 3 }}>
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <TextField
                  onChange={handleAuthorChange}
                  required
                  fullWidth
                  id="author"
                  label="Автор"
                  name="author"
                  autoComplete="author"
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  onChange={handleTitleChange}
                  required
                  fullWidth
                  name="title"
                  label="Название"
                  type="title"
                  id="title"
                  autoComplete="title"
                />
              </Grid>
              <Divider />
              <Grid item xs={12}>
                <div>
                  <Button
                    variant="contained"
                    component="label"
                    fullWidth
                  >
                    Выбрать файл Guitar Pro
                    <input
                      required
                      id="file"
                      type="file"
                      onChange={handleFileChange}
                      hidden
                    />
                  </Button>
                  <br />
                  <p>
                    {file ? file.name : ""}
                  </p>
                  {/* <Input
                  required
                  id="file"
                  type="file"
                  onChange={handleFileChange}
                /> */}
                  <Typography component='span' variant='span'>
                    Чтобы создать табулатуру, вам необходимо либо купить программное обеспечение Guitar Pro, либо использовать бесплатную альтернативу TuxGuitar (Файл → Сохранить как → Guitar Pro 5).
                  </Typography>
                </div>
              </Grid>
            </Grid>
            {file && <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
              onClick={handleUpload}
            >
              Загрузить
            </Button>}
          </Box>
        </Stack>

      </Box>
      {error}
    </Container>
  );
}
