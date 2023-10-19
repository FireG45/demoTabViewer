import { Component, createRef } from "react";
import Score from "./Score";
import Stack from "@mui/material/Stack";
import { Container } from "@mui/material";
import withRouter from './withRouter'
import { Link } from "react-router-dom";
import SimpleListMenu from "./SimpleListMenu";


class ShowTab extends Component {
  constructor(props) {
    super(props);
    this.id = null;
    this.track = 0;
    this.score = createRef()
    this.state = {
        error : null,
        isLoaded : false,
        tab: null,
    };
  }

  componentDidMount() {
    this.id = this.props.params.id
    this.track = this.props.params.track;
    console.log(this.track);
    var path = "http://localhost:8080/tabs/" + this.id + "?track=" + this.track
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
                error
            })
        }
    )
  }

  handleChange(event) {
    this.setState({
        selected: event.target.value,
    }, () => {
        this.props.history.push(`/${this.state.selected}/new/`)
    })
  }

  handleForceupdateMethod() {
    this.score.render();
  };

  render() {
    const {error, isLoaded, tab} = this.state;

    if (error) {
        return <p>Error: {error.meassage} </p>
    } else if (!isLoaded) {
        return <p>Loading...</p>
    } else {
      document.title = tab.author + ' - ' + tab.title + ' Tab';
      const tracks = tab.trackNames.map((track) => {
        var key = tab.trackNames.indexOf(track)
        var selectedStyle = key === this.track ? { 'font-weight' : 'bold'} : {};
        return (
          <Link key={key} style={selectedStyle} to={'http://localhost:3000/tabs/' + this.id + '/' + key} onClick={this.handleForceupdateMethod}>{track}</Link>
        )
      });

      return (
        <Container>
         <Stack direction="column" justifyContent="flex-start" alignItems="stretch" spacing={0} ml={-40} mr={-40}>
            <br></br>
            <h2>{tab.author} - {tab.title}</h2>
            <div>
              <SimpleListMenu options={tracks}/>
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