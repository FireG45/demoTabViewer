import { Paper, Rating, Grid, Typography, Stack, Pagination, Container, TextField, ListItemButton } from "@mui/material";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import CircleOutlinedIcon from '@mui/icons-material/CircleOutlined';
import CircleIcon from '@mui/icons-material/Circle';
import styled from "@emotion/styled";
import Checkbox from '@mui/material/Checkbox';
import FavoriteBorder from '@mui/icons-material/FavoriteBorder';
import Favorite from '@mui/icons-material/Favorite';

function TabList() {
  const [tabs, setTabs] = useState([]);
  const [page, setPage] = useState(1);
  const [pageCount, setPageCount] = useState(1);
  const [query, setQuery] = useState(null);
  const [queryStr, setQueryStr] = useState(null);
  const navigate = useNavigate();
  const params = useParams();

  useEffect(() => {
    document.title = params.author ? `Табулатуры ${params.author}` : 'Все табулатуры';
    fetch(`http://localhost:8080?page=${page - 1}&pageCount=${pageCount}${params.author ? `&author=${params.author}` : ''}${query ? `&query=${query}` : ''}`)
      .then((response) => response.json())
      .then((data) => {
        setTabs(data.tabs);
        setPage(data.page + 1);
        setPageCount(data.pageCount);
      })
      .catch((err) => {
        console.log(err.message);
      });
  }, [page, query]);

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
    <Stack spacing={3} alignItems={'center'}>
      <Container>
        {!!params.author &&
          <>
            <br />
            <Typography variant={'h5'} align={'center'}>
              Табулатуры {params.author}:
            </Typography>
          </>
        }
        {!!params.query &&
          <>
            <br />
            <Typography variant={'h5'} align={'center'}>
              Табулатуры по запросу: {params.query}:
            </Typography>
          </>
        }
        <br />
        {!!!params.author &&
          <TextField fullWidth type="search" id="search" label="Поиск..." value={queryStr}
            onKeyDown={(e) => {
              if (e.keyCode == 13) {
                setQueryStr(e.target.value)
                setQuery(e.target.value)
              }
            }}
            onChange={(e) => {
              setQueryStr(e.target.value)
            }}
            onClick={((e) => {
              setQuery("")
            })} />
        }
        <br /><br />
        {tabs.map((tab) => {
          console.log(tab.favorite);
          return (
            <div className="post-card" key={tab.id}>
              <ListItemButton onClick={() => navigate('/tabs/' + tab.id + '/0')} sx={paperSX} elevation={5} style={{ borderRadius: '5px', margin: "10px", padding: "15px", textAlign: "left" }}>
                <Grid container>
                  <Grid xs={11}>
                    <h3> {tab.author} - {tab.title} </h3>
                  </Grid>
                  <Grid xs={1}>
                    <Stack spacing={0.5} alignItems={'center'} alignContent={'center'} alignSelf={'center'}>
                      <StyledRating name="read-only" size="small"
                        icon={<CircleIcon fontSize="inherit" color="green" />}
                        emptyIcon={<CircleOutlinedIcon fontSize="inherit" color="green" />} value={tab.rating} readOnly />
                    </Stack>
                  </Grid>
                </Grid>
              </ListItemButton>
            </div>
          );
        })}
      </Container>
      <Pagination page={page} count={pageCount} onChange={(_, newPage) => {
        setPage(newPage)
      }} color="primary" />
      <br />
    </Stack>
  );
}
export default TabList;