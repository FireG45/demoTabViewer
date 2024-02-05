import * as React from 'react';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { Alert, Divider, Input, Stack } from '@mui/material';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { useCookies } from "react-cookie";

export default function UpdateTab() {
  const [cookies, setCookie, removeCookie] = useCookies(["token"]);
  const [file, setFile] = React.useState(null);
  const [title, setTitle] = React.useState("");
  const [author, setAuthor] = React.useState("");
  const [error, setError] = React.useState(null);
  const [loaded, setLoaded] = React.useState(false);
  const params = useParams();

  const navigate = useNavigate();

  const handleFileChange = (e) => {
    if (e.target.files) {
      setFile(e.target.files[0]);
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

  const getParams = async () => {
    try {
      const result = await fetch("http://localhost:8080/tabs/update/" + params.id, {
        method: "GET",
        headers: new Headers({
          'Authorization': 'Bearer ' + cookies["token"]
        })
      });

      if (result.ok) {
        const data = await result.json();
        setAuthor(data.author)
        setTitle(data.title)
        console.log(author);
        console.log(title);
        setLoaded(true)
      }
    } catch (error) {
      console.error(error);
    }
  }

  React.useEffect(() => {
    if (!loaded) getParams()
  })


  const handleUpload = async (e) => {
    e.preventDefault()
    if (true) {
      console.log("Uploading file...");
      const formData = new FormData();
      formData.append("file", file ? file : "null");
      formData.append("author", author);
      formData.append("title", title);

      try {
        const result = await fetch("http://localhost:8080/tabs/update/" + params.id, {
          method: "PATCH",
          body: formData,
          headers: new Headers({
            'Authorization': 'Bearer ' + cookies["token"]
          })
        });


        if (result.ok) {
          const data = await result.json();
          navigate('/tabs/' + data.id + '/0')
        } else {
          if (result.status !== 200) {
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
            Редактировать табулатуру!
          </Typography>
          <Divider />
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
                  value={author}
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
                  value={title}
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
                    Если хотите оставить файл без изменений, отсавте поле файла пустым.
                  </Typography>
                </div>
              </Grid>
            </Grid>
            {<Button
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
