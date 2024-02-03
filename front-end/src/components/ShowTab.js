import { Component, createRef } from "react";
import Score from "./Score";
import Stack from "@mui/material/Stack";
import { Container } from "@mui/material";
import withRouter from './withRouter'
import { CircularProgress } from "@mui/material";
import Box from '@mui/material/Box';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';

class ShowTab extends Component {
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
    this.state = {
        error : null,
        isLoaded : false,
        tab: null,
        track : 0,
    };
  }

  componentDidMount() {
    this.id = this.props.params.id
    this.track = this.props.params.track
    var path = "http://localhost:8080/tabs/" + this.id + "?track=" + this.track
    this.path = "/tabs/" + this.props.params.id + "/"  
    fetch(path).then(res => res.json()).then(
        (result) => {
            this.setState({
                isLoaded : true,
                tab: result
            });
        },
        (error) => {
            this.setState({
                isLoaded: true,
                track : this.track,
                error
            })
        }
    )
  }

  render() {
    const {error, isLoaded, tab} = this.state;
    console.log(sessionStorage.getItem("token"));

    if (error) {
        return <p>Error: {error.meassage} </p>
    } else if (!isLoaded) {
        return <CircularProgress />
    } else {
      document.title = tab.author + ' - ' + tab.title + ' Tab';
      const tracks = tab.trackNames.map((track) => {
        return track
      });

      return (
        <Container>
         <Stack direction="column" justifyContent="flex-start" alignItems="stretch" spacing={0} ml={-40} mr={-40}>
            <br></br>
            <h2>{tab.author} - {tab.title}</h2>
            <div>
              <Box sx={{ maxWidth: 220 }}>
                <FormControl fullWidth>
                  <InputLabel id="demo-simple-select-label">Track</InputLabel>
                  <Select
                    labelId="demo-simple-select-label"
                    id="demo-simple-select"
                    value={this.track}
                    label="Track"
                    onChange={
                      (event) => { 
                        this.props.navigate(this.path + event.target.value, { replace: false });
                        this.forceUpdate();
                        this.props.navigate(0);
                    }
                  }
                  >
                    {
                      Array.from(Array(tracks.length)).map((_, index) => {
                        return <MenuItem key={index} value={index}>{tracks[index]}</MenuItem>
                      })
                    }
                  </Select>
                </FormControl>
              </Box>
            </div>
            <div>
              <Score ref={this.score} id={this.id} track={this.track} />
            </div>
          </Stack>
        </Container>
      )
    }
  }
}

export default withRouter(ShowTab);