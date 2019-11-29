namespace java thrift.generated
namespace py py.thrift.generated
typedef i32 int
typedef i64 long

struct Person{
 1: optional string username,
 2: optional int age,
 3: optional bool married
}

exception DataException{
  1: optional string message,
  2: optional string callStack,
  3: optional string  date
}

service PersonService{
   Person getByUserName(1:required string username)throws(1:DataException dataException) ,
   void savePerson(1:required Person person)throws(1:DataException dataException)
}