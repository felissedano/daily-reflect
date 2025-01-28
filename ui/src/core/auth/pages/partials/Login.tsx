import {FaceIcon} from "@radix-ui/react-icons"
import {Button, Text} from "@radix-ui/themes";

const Login = ({handleLogin}: { handleLogin: String }) => {

    return (
        <>
            <Button>
                <FaceIcon/>
                <Text>{handleLogin}</Text>
            </Button>
        </>
    )
}

export default Login
