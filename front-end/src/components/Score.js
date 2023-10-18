import { Component } from "react";
import Stave from "./Stave";

export default class Score extends Component {
    constructor(props) {
        super(props);
        this.id = props.id
        this.state = {
            error : null,
            isLoaded : false,
            measures: []
        };
    }

    componentDidMount() {
        var path = "http://localhost:8080/tabs/" + this.id
        fetch(path).then(res => res.json()).then(
            (result) => {
                this.setState({
                    isLoaded : true,
                    measures: result.measures
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

    render() {
        const {error, isLoaded, measures} = this.state;
        if (error) {
            return <p>Error: {error.meassage} </p>
        } else if (!isLoaded) {
            return <p>Loading...</p>
        } else {
            return (
                <Stave measures={measures}/>
            )
        }
    }
}