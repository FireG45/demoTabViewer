import {Component, createRef} from "react";
import Score from "./Score";
import Stack from "@mui/material/Stack";
import {
    Alert, Button,
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
import EditScore from "./EditScore";
import AppBar from "@mui/material/AppBar";
import Dialog from "@mui/material/Dialog";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";
import * as React from "react";

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

class EditTab extends Component {
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
            open: false,
            reload: false,
        };

        window.addEventListener('beforeunload', (event) => {
            if (this.state.reload) return;
            event.preventDefault();
            //this.handleClickOpen();
            //this.handleClickOpen();
        });


        this.handleClickOpen = () => {
            this.setState({
                open: true,
            });
        };

        this.handleClose = () => {
            this.setState({
                open: false,
            });
        };

        this.handleUpload = async (changes) => {
            let changesString = JSON.stringify(changes)
            try {
                const result = await fetch("http://localhost:8080/edit/" + this.id + "/" + this.track, {
                    method: "POST",
                    body: changesString,
                    headers: new Headers({
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + this.props.cookies.get("token")
                    })
                });

                if (result.ok) {
                    this.props.navigate('/tabs/' + this.id + '/' + this.track)
                } else {
                    if (result.status !== 200) {
                        this.props.cookies.remove("token")
                        this.props.cookies.remove("user")
                        this.props.navigate('/signin')
                    }
                }
            } catch (error) {
                console.error(error);
            }
        };
    }

    componentDidMount() {
        const {cookies} = this.props;
        this.token = cookies.get("token")
        this.id = this.props.params.id
        this.track = this.props.params.track
        var path = "http://localhost:8080/tabs/" + this.id + "?track=" + this.track
        this.path = "/edit/" + this.props.params.id + "/"
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
                                <h2>{tab.author} - {tab.title}</h2>
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
                            <EditScore ref={this.score} id={this.id} track={this.track}/>
                        </div>
                    </Stack>
                </Container>
                <br/><br/>
                <br/><br/>
                <br/><br/>
                <br/><br/>
                <br/><br/>

                <Dialog
                    open={this.state.open}
                    onClose={this.handleClose}
                    aria-labelledby="alert-dialog-title"
                    aria-describedby="alert-dialog-description"
                >
                    <DialogTitle id="alert-dialog-title">
                        {"Обновить страницу?"}
                    </DialogTitle>
                    <DialogContent>
                        <DialogContentText id="alert-dialog-description">
                            После обновления страницы не сохранённые изменения будут потеряны
                        </DialogContentText>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={this.handleClose} autoFocus>Отмена</Button>
                        <Button onClick={() => {
                            this.setState({
                                reload: true,
                            });
                            window.reload();
                        }}>
                            Обновить
                        </Button>
                    </DialogActions>
                </Dialog>

                <AppBar position="fixed" sx={{top: 'auto', bottom: 0}}>
                    <form>
                        <Button fullWidth variant={'contained'} onClick={async () => {
                            let changes = this.score.current.getChanges();
                            let staveChanges = this.score.current.getStavesChanges();
                            await this.handleUpload({
                                noteChanges: changes,
                                staveChanges: staveChanges
                            });
                        }}>
                            Сохранить
                        </Button>
                    </form>
                </AppBar>
            </>)
        }
    }
}

export default withCookies(withRouter(EditTab));