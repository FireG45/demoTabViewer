import React from 'react';
import { Box, Button, Container, Stack } from '@mui/material';
import Grid from '@mui/material/Grid';
import { useNavigate } from 'react-router-dom';

export default function NotFoundError() {
  const navigate = useNavigate()

  return (
    <Box
      sx={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        minHeight: '100vh'
      }}
    >
      <Container maxWidth="md">
        <Stack container justifyItems='center'>
          <Grid>
            <img
              src="https://cdn.dribbble.com/users/1408464/screenshots/6377404/404_illustration.png"
              alt=""
              width={800} height={600}
            />
          </Grid>
          <Grid> 
            <Button variant="contained" onClick={() => navigate('/')}>Back Home</Button>
          </Grid>
        </Stack>
      </Container>
    </Box>
  );
}