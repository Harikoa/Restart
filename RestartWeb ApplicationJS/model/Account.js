class Account{
   
    constructor(id,email,password,firstName,lastName,middleName,nickname,role,sex,lastAssessment,FCM,connectedUser,substanceUsed,birthDay,lastSuspensionDay,activated,important)
    {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.nickname = nickname;
        this.role=role
        this.sex=sex
        this.lastAssessment = lastAssessment
        this.FCM = FCM
        this.connectedUser = connectedUser
        this.substanceUsed = substanceUsed
        this.birthDay = birthDay
        this.lastSuspensionDay = lastSuspensionDay
        this.activated = activated
        this.important = important
    }
    
}
module.exports = Account;