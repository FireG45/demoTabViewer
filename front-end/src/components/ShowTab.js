import { Component, createRef } from "react";
import Score from "./Score";
import Stack from "@mui/material/Stack";
import { AppBar, ButtonGroup, Container, Grid, IconButton, OutlinedInput, Rating, Slider, Toolbar, Typography, makeStyles } from "@mui/material";
import withRouter from './withRouter'
import Box from '@mui/material/Box';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import TabInfoPopover from "./TabInfoPopover";
import { withCookies, Cookies } from "react-cookie";
import Loading from "./Loading";
import { instanceOf } from "prop-types";
import CircleOutlinedIcon from '@mui/icons-material/CircleOutlined';
import CircleIcon from '@mui/icons-material/Circle';
import styled from "@emotion/styled";
import { Link } from "react-router-dom";
import Checkbox from '@mui/material/Checkbox';
import FavoriteBorder from '@mui/icons-material/FavoriteBorder';
import Favorite from '@mui/icons-material/Favorite';
import PlayArrowIcon from '@mui/icons-material/PlayArrow';
import EditIcon from '@mui/icons-material/Edit';
import SpeedIcon from '@mui/icons-material/Speed';
import AvTimerIcon from '@mui/icons-material/AvTimer';
import { IconMetronome } from '@tabler/icons-react';

const label = { inputProps: { 'aria-label': 'Checkbox demo' } };

class ShowTab extends Component {
  static propTypes = {
    cookies: instanceOf(Cookies).isRequired
  };

  state = {
    user: this.props.cookies.get("user") || "",
    token: this.props.cookies.get("token") || "",
    playing: false,
  };

  handleSetCookie = () => {
    const { cookies } = this.props;
    cookies.set("user", "obydul", { path: "/" }); // set the cookie
    this.setState({ user: cookies.get("user") });
  };

  handleRemoveCookie = () => {
    const { cookies } = this.props;
    cookies.remove("user"); // remove the cookie
    this.setState({ user: cookies.get("user") });
  };

  routingFunction = (event) => {
    var path = "/tabs/" + this.id + "?track=" + event.target.value
    this.props.history.push({
      pathname: path,
      state: event.target.value
    });
  }

  constructor(props) {
    super(props);
    this.id = null;
    this.score = createRef()
    this.track = 0;
    this.token = "";
    this.state = {
      error: null,
      isLoaded: false,
      tab: null,
      track: 0,
      value: 0,
      favorite: false,
      speed: 1
    };
  }

  componentDidMount() {
    const { cookies } = this.props;
    this.token = cookies.get("token")
    this.id = this.props.params.id
    this.track = this.props.params.track
    var path = "http://localhost:8080/tabs/" + this.id + "?track=" + this.track
    this.path = "/tabs/" + this.props.params.id + "/"
    fetch(path, {
      headers: new Headers(this.token ? {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + this.token
      } : {
        'Content-Type': 'application/json',
      }
      )
    }).then(res => res.json()).then(
      (result) => {
        this.setState({
          isLoaded: true,
          tab: result,
          favorite: result.favorite,
        });
      },
      (error) => {
        this.setState({
          isLoaded: true,
          track: this.track,
          error
        })
      }
    )
  }


  render() {
    const { error, isLoaded, tab, value, favorite, speed } = this.state;

    if (error) {
      return <p>Error: {error.meassage} </p>
    } else if (!isLoaded) {
      return <Loading />
    } else {
      document.title = tab.author + ' - ' + tab.title + ' Tab';
      const tracks = tab.trackNames.map((track) => {
        return track
      });

      console.log(tab.userOwner);

      const StyledRating = styled(Rating)({
        '& .MuiRating-iconFilled': {
          color: '#2979ff',
        },
        '& .MuiRating-iconHover': {
          color: '#2979ff',
        },
      });

      return (
        <>
          <AppBar position="fixed" sx={{ top: 'auto', bottom: 0 }}>
            <Toolbar>
              <Stack direction={'row'} alignItems="center" spacing={30}>
                <FormControl sx={{ m: 1, width: 300 }} variant={'filled'} >
                  <Select
                    sx={{ color: 'white' }}
                    labelId="demo-multiple-name-label"
                    id="demo-multiple-name"
                    value={this.track}
                    onChange={
                      (event) => {
                        this.props.navigate(this.path + event.target.value, { replace: false });
                        this.forceUpdate();
                        this.props.navigate(0);
                      }
                    }
                    input={<OutlinedInput label="Name" />}
                  >
                    {
                      Array.from(Array(tracks.length)).map((_, index) => {
                        return <MenuItem key={index} value={index}>
                          <Typography>
                            {tracks[index]}
                          </Typography>
                        </MenuItem>
                      })
                    }
                  </Select>
                </FormControl>
                <Stack spacing={2} direction={'row'}>
                  <IconButton color="inherit">
                    <PlayArrowIcon />
                  </IconButton>
                  <Stack spacing={2} direction="row" sx={{ mb: 1, width: 200 }} alignItems="center">
                    <SpeedIcon />
                    <Box sx={{ minWidth: 120 }}>
                      <FormControl fullWidth>
                        <Select
                          sx={{ color: 'white' }}
                          labelId="demo-simple-select-label"
                          id="demo-simple-select"
                          value={speed}
                          onChange={(e) => {
                            this.setState({
                              speed: e.target.value
                            })
                          }}
                        >
                          <MenuItem value={0.1}>10% </MenuItem>
                          <MenuItem value={0.2}>20% </MenuItem>
                          <MenuItem value={0.3}>30% </MenuItem>
                          <MenuItem value={0.4}>40% </MenuItem>
                          <MenuItem value={0.5}>50% </MenuItem>
                          <MenuItem value={0.6}>60% </MenuItem>
                          <MenuItem value={0.7}>70% </MenuItem>
                          <MenuItem value={0.8}>80% </MenuItem>
                          <MenuItem value={0.9}>90% </MenuItem>
                          <MenuItem value={1.0}>100%</MenuItem>
                        </Select>
                      </FormControl>
                    </Box>
                  </Stack>
                </Stack>
                <IconButton color="inherit">
                  <AvTimerIcon />
                </IconButton>
                <IconButton color="inherit">
                  <IconMetronome />
                </IconButton>
                <IconButton color="inherit">
                  <EditIcon />
                </IconButton>
              </Stack>
            </Toolbar>
          </AppBar>

          <Container>
            <Stack direction="column" justifyContent="flex-start" alignItems="stretch" spacing={0} ml={-40} mr={-40}>
              <br></br>
              <Grid container spacing={0}>
                <Grid xs={6}>
                  <h2><Link to={`/tabs/${tab.author}`}>{tab.author}</Link> - {tab.title}</h2>
                </Grid>
                <Grid xs={6} style={{ textAlign: "right" }}>
                  <ButtonGroup>
                    <Checkbox onChange={async (_, cheked) => {
                      try {
                        const result = await fetch(`http://localhost:8080/tabs/${cheked ? "setfavorite" : "removefavorite"}/` + this.id, {
                          method: cheked ? "POST" : "DELETE",
                          headers: new Headers({
                            'Content-Type': 'application/json',
                            'Authorization': 'Bearer ' + this.token
                          })
                        });
                        if (result.ok) {
                          this.setState({
                            favorite: cheked
                          });
                        }
                      } catch (error) {
                        console.error(error);
                      }
                    }} checked={favorite} icon={<FavoriteBorder />} checkedIcon={<Favorite />} />
                    <TabInfoPopover data={{ user: tab.user, uploaded: tab.uploaded, id: this.id, owner: tab.userOwner }} />
                  </ButtonGroup>
                  <Typography component="legend">Сложность табулатуры:</Typography>
                  <StyledRating
                    name="simple-controlled"
                    icon={<CircleIcon fontSize="inherit" color="green" />}
                    emptyIcon={<CircleOutlinedIcon fontSize="inherit" color="green" />}
                    value={value || tab.rating}
                    onChange={async (event, newValue) => {
                      const formData = new FormData();
                      formData.append("tabId", this.id);
                      formData.append("value", newValue);
                      try {
                        const result = await fetch("http://localhost:8080/addreview", {
                          method: "POST",
                          body: JSON.stringify({
                            "tabId": this.id,
                            "value": newValue
                          }),
                          headers: new Headers({
                            'Content-Type': 'application/json',
                            'Authorization': 'Bearer ' + this.token
                          })
                        });

                        if (result.ok) {
                          this.setState({
                            value: newValue
                          });
                        }
                      } catch (error) {
                        console.error(error);
                      }
                    }}
                  />
                </Grid>
              </Grid>
              <div>
                <Score ref={this.score} id={this.id} track={this.track} />
              </div>
            </Stack>
          </Container>
        </>

      )
    }
  }
}

export default withCookies(withRouter(ShowTab));