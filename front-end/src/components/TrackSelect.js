import * as React from 'react';
import Box from '@mui/material/Box';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import withRouter from './withRouter'

function TrackSelect({options = [], trackId = 0, path = '', handleClick = null}) {
  const [track = trackId] = React.useState('');

  return (
    <Box sx={{ maxWidth: 220 }}>
      <FormControl fullWidth>
        <InputLabel id="demo-simple-select-label">Track</InputLabel>
        <Select
          labelId="demo-simple-select-label"
          id="demo-simple-select"
          value={track}
          label="Track"
          onChange={handleClick}
        >
          {
            Array.from(Array(options.length)).map((_, index) => {
              return <MenuItem key={index} value={index}>{options[index]}</MenuItem>
            })
          }
        </Select>
      </FormControl>
    </Box>
  );
}

export default withRouter(TrackSelect);