import React, {useEffect, useState} from 'react';
import Box from '@mui/material/Box';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import PlayArrowIcon from '@mui/icons-material/PlayArrow';
import SpeedIcon from '@mui/icons-material/Speed';
import AvTimerIcon from '@mui/icons-material/AvTimer';
import {IconMetronome} from '@tabler/icons-react';
import {AppBar, IconButton, Stack, Toolbar} from "@mui/material";
import MidiWebPlayer from "./midiUtils/MidiWebPlayer";
import {Pause, Stop} from "@mui/icons-material";


export default function TabPlayer({id = 0, score = null }) {
    const [speed, setSpeed] = useState(1.0);
    const [loaded, setLoaded] = useState(false);

    var measuresLength = []

    var measures = null

    const newMidiPlayer = () => new MidiWebPlayer('http://localhost:8080/tabs/midi/' + id, score,
        measures, measuresLength)

    const [midiPlayer, setMidiPlayer] =
        useState(newMidiPlayer())

    const self = this;


    useEffect(() => {
        setMidiPlayer(newMidiPlayer())
        midiPlayer.handleMidi();
        midiPlayer.setSpeed(speed);
        setLoaded(true);
    }, [speed]);

    return (
        <>
            <AppBar position="fixed" sx={{top: 'auto', bottom: 0}}>
                <Toolbar>
                    <Stack direction={'row'} alignItems="center" spacing={175}>
                        <Stack spacing={2} direction={'row'}>
                            <IconButton color="inherit" onClick={() => {
                                midiPlayer.play();
                                midiPlayer.setSpeed(speed);
                            }}>
                                <PlayArrowIcon/>
                            </IconButton>
                            <IconButton color="inherit" onClick={midiPlayer.pause}>
                                <Pause/>
                            </IconButton>
                            <IconButton color="inherit" onClick={() => {
                                setMidiPlayer(newMidiPlayer());
                                midiPlayer.handleMidi();
                                midiPlayer.setSpeed(speed);
                                setLoaded(true);
                            }}>
                                <Stop/>
                            </IconButton>
                            <Stack spacing={2} direction="row" sx={{mb: 1, width: 200}} alignItems="center">
                                <SpeedIcon/>
                                <Box sx={{minWidth: 120}}>
                                    <FormControl fullWidth>
                                        <Select
                                            sx={{color: 'white'}}
                                            labelId="demo-simple-select-label"
                                            id="demo-simple-select"
                                            value={speed}
                                            onChange={(e) => {
                                                setSpeed(e.target.value);
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


                        <Stack spacing={2} direction="row">
                            <IconButton color="inherit">
                                <AvTimerIcon/>
                            </IconButton>
                            <IconButton color="inherit">
                                <IconMetronome/>
                            </IconButton>
                            {/*<IconButton color="inherit">*/}
                            {/*    <EditIcon/>*/}
                            {/*</IconButton>*/}
                        </Stack>
                    </Stack>
                </Toolbar>
            </AppBar>
        </>
    )

}