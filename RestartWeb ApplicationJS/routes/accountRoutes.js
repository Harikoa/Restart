const express = require("express")
const router = express.Router()
const {addAcc,getAllAcc} = require("../controller/accountController")

router.get("/",getAllAcc)

module.exports = router