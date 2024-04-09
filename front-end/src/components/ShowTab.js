import {Component, createRef} from "react";
import Score from "./Score";
import Stack from "@mui/material/Stack";
import {
    Alert,
    ButtonGroup, Container, FormControl, Grid, MenuItem, OutlinedInput, Rating, Select, Snackbar, Typography
} from "@mui/material";
import withRouter from './withRouter'

import TabInfoPopover from "./TabInfoPopover";
import {withCookies, Cookies} from "react-cookie";
import Loading from "./Loading";
import {instanceOf} from "prop-types";
import CircleOutlinedIcon from '@mui/icons-material/CircleOutlined';
import CircleIcon from '@mui/icons-material/Circle';
import styled from "@emotion/styled";
import {Link} from "react-router-dom";
import Checkbox from '@mui/material/Checkbox';
import FavoriteBorder from '@mui/icons-material/FavoriteBorder';
import Favorite from '@mui/icons-material/Favorite';
import TabPlayer from "./TabPlayer";
import InputLabel from "@mui/material/InputLabel";

function convertDuration(duration) {
    let dDotted = 0;
    dDotted += duration[0] === '.' ? 1 : 0;
    dDotted += duration[1] === '.' ? 1 : 0;
    duration = duration.slice(dDotted);
    let durationValue = 0.0;
    switch (duration) {
        case "w" :
            durationValue = 1;
            break;
        case "h" :
            durationValue = 1 / 2;
            break;
        case "q" :
            durationValue = 1 / 4;
            break;
        default :
            durationValue = 1 / parseInt(duration);
            break;
    }
    for (let i = 0; i < dDotted; i++) durationValue += (durationValue / 2);
    return durationValue;
}

class ShowTab extends Component {
    static propTypes = {
        cookies: instanceOf(Cookies).isRequired
    };

    state = {
        user: this.props.cookies.get("user") || "", token: this.props.cookies.get("token") || "", playing: false,
        snackbarOpen: false,
    };

    handleSetCookie = () => {
        const {cookies} = this.props;
        cookies.set("user", "obydul", {path: "/"}); // set the cookie
        this.setState({user: cookies.get("user")});
    };

    handleRemoveCookie = () => {
        const {cookies} = this.props;
        cookies.remove("user"); // remove the cookie
        this.setState({user: cookies.get("user")});
    };

    routingFunction = (event) => {
        var path = "/tabs/" + this.id + "?track=" + event.target.value
        this.props.history.push({
            pathname: path, state: event.target.value
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
            note: 0,
            favorite: false,
            speed: 1,
            tabBeats: [],
        };
    }

    componentDidMount() {
        const {cookies} = this.props;
        this.token = cookies.get("token")
        this.id = this.props.params.id
        this.track = this.props.params.track
        var path = "http://localhost:8080/tabs/" + this.id + "?track=" + this.track
        this.path = "/tabs/" + this.props.params.id + "/"
        fetch(path, {
            headers: new Headers(this.token ? {
                'Content-Type': 'application/json', 'Authorization': 'Bearer ' + this.token
            } : {
                'Content-Type': 'application/json',
            })
        }).then(res => res.json()).then((result) => {
            let beats = []

            let metronomeTrack = {
                "n": 37,
                "notes": [],
                "volume": 10,
                "info": {
                    "variable": "_drum_37_0_SBLive_sf2",
                    "url": "https://surikov.github.io/webaudiofontdata/sound/12837_0_SBLive_sf2.js",
                    "pitch": 37,
                    "title": "Side Stick/Rimshot"
                }
            }

            let metronomeBeats = []
            let last = 0.0;
            let metronomeLast = 0.0;
            let measures = [...result.measures];
            let measuresStarts = []
            let measuresStartNotesInds = []
            let measuresDurations = []
            for (let i = 0; i < measures.length; i++) {
                let measureDuration = 0.0;
                measuresStartNotesInds.push(beats.length);
                let measure = measures[i];
                let bpm = measure.tempo;
                let timeSignature = parseInt(measure.timeSignature.split('/')[0]);
                let basicDuration = parseInt(measure.timeSignature.split('/')[1]);
                measuresStarts.push(last);
                for (let j = 0; j < measure.beatDTOS.length; j++) {
                    let beat = measure.beatDTOS[j];
                    let noteDuration = convertDuration(beat.duration);
                    let duration
                        = 60.0 / (bpm * (1 / 4) / (noteDuration / (1 / timeSignature)) * timeSignature);
                    beats.push({when: last, duration: duration, pause: beat.noteDTOS.length === 0});
                    last += duration;
                }

                for (let j = 0; j < timeSignature; j++) {
                    let duration
                        = 60.0 / (bpm * (1 / 4) / ((1 / basicDuration) / (1 / timeSignature)) * timeSignature);
                    metronomeTrack.notes.push({when: metronomeLast});
                    metronomeLast += duration;
                    measureDuration += duration;
                }
                measuresDurations.push(measureDuration);
            }

            this.setState({
                isLoaded: true, tab: result, favorite: result.favorite, tabBeats: beats,
                metronomeTrack: metronomeTrack, measuresStarts: measuresStarts,
                measuresStartNotesInds: measuresStartNotesInds, measuresDurations: measuresDurations
            });
        }, (error) => {
            this.setState({
                isLoaded: true, track: this.track, error
            })
        })
    }

    render() {
        console.log("ShowTab render")
        const {error, isLoaded, tab, value, favorite, speed} = this.state;

        if (error) {
            return <p>Error: {error.message} </p>
        } else if (!isLoaded) {
            return <Loading/>
        } else {
            document.title = tab.author + ' - ' + tab.title;
            const tracks = tab.trackNames.map((track) => {
                return track
            });

            console.log(tab.userOwner);

            const StyledRating = styled(Rating)({
                '& .MuiRating-iconFilled': {
                    color: '#2979ff',
                }, '& .MuiRating-iconHover': {
                    color: '#2979ff',
                },
            });

            return (<>
                <TabPlayer score={this.score} id={this.id} tabBeats={this.state.tabBeats}
                           metronomeTrack={this.state.metronomeTrack} measuresStarts={this.state.measuresStarts}
                           measuresStartNotesInds={this.state.measuresStartNotesInds}
                           measuresDurations={this.state.measuresDurations} measures={this.state.tab.measures}/>

                <Snackbar open={this.state.snackbarOpen}
                          autoHideDuration={6000}
                          anchorOrigin={{vertical: 'top', horizontal: 'right'}}
                          onClose={(event, reason) => {
                              if (reason === 'clickaway') {
                                  return;
                              }
                              this.setState({snackbarOpen: false});
                          }}>
                    <Alert
                        severity="error"
                        variant="filled"
                        sx={{width: '100%'}}
                    >
                        Вы не авторизированы!
                    </Alert>
                </Snackbar>
                <Container>
                    <Stack direction="column" justifyContent="flex-start" alignItems="stretch" spacing={0} ml={-40}
                           mr={-40}>
                        <br></br>
                        <Grid container spacing={0}>
                            <Grid xs={6}>
                                <h2><Link to={`/tabs/${tab.author}`}>{tab.author}</Link> - {tab.title}</h2>
                            </Grid>
                            <Grid xs={6} style={{textAlign: "right"}}>
                                <ButtonGroup>
                                    <Checkbox onChange={async (_, cheked) => {
                                        if (!this.token) {
                                            this.setState({snackbarOpen: true});
                                            return;
                                        }
                                        try {
                                            const result = await fetch(`http://localhost:8080/tabs/${cheked ? "setfavorite" : "removefavorite"}/` + this.id, {
                                                method: cheked ? "POST" : "DELETE", headers: new Headers({
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
                                    }} checked={favorite} icon={<FavoriteBorder/>} checkedIcon={<Favorite/>}/>
                                    <TabInfoPopover data={{
                                        user: tab.user, uploaded: tab.uploaded, id: this.id, owner: tab.userOwner
                                    }}/>
                                </ButtonGroup>
                                <Typography component="legend">Сложность табулатуры:</Typography>
                                <StyledRating
                                    name="simple-controlled"
                                    icon={<CircleIcon fontSize="inherit" color="green"/>}
                                    emptyIcon={<CircleOutlinedIcon fontSize="inherit" color="green"/>}
                                    value={value || tab.rating}
                                    onChange={async (event, newValue) => {
                                        if (!this.token) {
                                            this.setState({snackbarOpen: true});
                                            return;
                                        }
                                        const formData = new FormData();
                                        formData.append("tabId", this.id);
                                        formData.append("value", newValue);
                                        try {
                                            const result = await fetch("http://localhost:8080/addreview", {
                                                method: "POST", body: JSON.stringify({
                                                    "tabId": this.id, "value": newValue
                                                }), headers: new Headers({
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
                        <FormControl sx={{m: 1, width: 500}} variant={'filled'}>
                            <InputLabel id="demo-simple-select-label">Трек</InputLabel>
                            <Select
                                labelId="demo-multiple-name-label"
                                id="demo-multiple-name"
                                value={this.track}
                                label={'Трек'}
                                onChange={(event) => {
                                    this.props.navigate(this.path + event.target.value, {replace: false});
                                    this.forceUpdate();
                                    this.props.navigate(0);
                                }}
                                input={<OutlinedInput label="Name"/>}
                            >
                                {Array.from(Array(tracks.length)).map((_, index) => {
                                    return <MenuItem key={index} value={index}>
                                        <Typography>
                                            {tracks[index]}
                                        </Typography>
                                    </MenuItem>
                                })}
                            </Select>
                        </FormControl>
                        <div>
                            <Score ref={this.score} id={this.id} track={this.track}/>
                        </div>
                    </Stack>
                </Container>
                <br/><br/>
                <br/><br/>
                <br/><br/>
                <br/><br/>
                <br/><br/>
            </>)
        }
    }
}

export default withCookies(withRouter(ShowTab));