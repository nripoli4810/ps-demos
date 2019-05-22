import React from 'react';
import PropTypes from 'prop-types';

const styles = {
    content: {
        fontSize: '35px',
        position: 'absolute',
        left: '0',
        right: '0',
        marginTop: '20px',
        textAlign: 'center'
    }
}

export default class Loading extends React.Component {
    state = {
        content: this.props.text,
        speed: this.props.speed
    }


    componentDidMount() {
        const { content, speed } = this.state

        this.internval = window.setInterval(() => {
            this.state.content === content + '...'
                ? this.setState({ content: content })
                : this.setState(({ content }) => ({ content: content + '.' }))
        }, speed)
    }

    componentWillUnmount() {
        window.clearInterval(this.internval)
    }

    render() {
        return (
            <p style={styles.content}>
                {this.state.content}
            </p>
        )
    }
}

Loading.propTypes = {
    text: PropTypes.string.isRequired,
    speed: PropTypes.number.isRequired
}

Loading.defaultProps = {
    text: 'Loading',
    speed: 300
}